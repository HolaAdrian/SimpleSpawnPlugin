package de.adrian.simpleSpawn.Commands;

import de.adrian.simpleSpawn.SimpleSpawn;
import de.adrian.simpleSpawn.Utility.TranslationManager;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SpawnCommand implements CommandExecutor {
    private BukkitTask teleportTask;
    private int countdowntime = 0;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        // Check if sender is player
        if (!(sender instanceof Player)) {
            sender.sendMessage(TranslationManager.get("must_be_player_spawn"));
            return false;
        }

        Player player = (Player) sender;

        // Permission check
        if (!player.hasPermission("simplespawn.spawn")) {
            player.sendMessage(TranslationManager.get("no_permission_spawn"));
            return false;
        }

        // Config validation
        FileConfiguration config = SimpleSpawn.main.getConfig();
        if (config == null) {
            player.sendMessage(TranslationManager.get("no_config"));
            return false;
        }

        if (config.getString("prefix") == null) {
            player.sendMessage(TranslationManager.get("prefix_corrupt"));
            return false;
        }

        FileConfiguration custom = UtilityTools.getCustomConfig();
        if (custom == null) {
            player.sendMessage(TranslationManager.get("no_spawn_file"));
            return false;
        }

        if (!custom.isSet("spawn.world")) {
            player.sendMessage(TranslationManager.get("no_spawn_set"));
            return false;
        }

        // Ground check
        if (!config.isBoolean("ground")) {
            player.sendMessage(TranslationManager.get("setting_corrupt", "ground"));
            return false;
        }

        if (config.getBoolean("ground") && !player.isOnGround()) {
            player.sendMessage(TranslationManager.get("ground_required"));
            return false;
        }

        // Cancel any existing teleport
        if (teleportTask != null) {
            teleportTask.cancel();
        }

        countdowntime = 0;
        processTeleport(player);
        return true;
    }

    private void processTeleport(Player player) {
        FileConfiguration config = SimpleSpawn.main.getConfig();

        // Check if cooldown is required
        if (!config.isBoolean("cooldownrequirement")) {
            player.sendMessage(TranslationManager.get("setting_corrupt", "cooldownrequirement"));
            return;
        }

        // Instant teleport if no cooldown
        if (!config.getBoolean("cooldownrequirement")) {
            executeTeleport(player);
            return;
        }

        // Validate cooldown setting
        if (!config.isInt("cooldown")) {
            player.sendMessage(TranslationManager.get("cooldown_spawn_corrupt"));
            return;
        }

        // Start cooldown process
        int cooldown = config.getInt("cooldown");
        Location initialLocation = player.getLocation();

        teleportTask = Bukkit.getScheduler().runTaskTimer(SimpleSpawn.main, () -> {
            if (countdowntime >= cooldown) {
                executeTeleport(player);
                teleportTask.cancel();
                return;
            }

            // Check if player moved
            if (hasMoved(player, initialLocation)) {
                player.sendActionBar(Component.text(
                        TranslationManager.get("cooldown_spawn_canceled")
                ));
                teleportTask.cancel();
                return;
            }

            countdowntime++;
            int timeLeft = cooldown - countdowntime + 1;
            player.sendActionBar(Component.text(
                    TranslationManager.get("cooldown_spawn_start", timeLeft + "")
            ));
        }, 0L, 20L);
    }

    private boolean hasMoved(Player player, Location initialLoc) {
        Location current = player.getLocation();
        return Math.round(initialLoc.getX()) != Math.round(current.getX()) ||
                Math.round(initialLoc.getY()) != Math.round(current.getY()) ||
                Math.round(initialLoc.getZ()) != Math.round(current.getZ());
    }

    private void executeTeleport(Player player) {
        FileConfiguration custom = UtilityTools.getCustomConfig();
        World world = Bukkit.getWorld(custom.getString("spawn.world"));

        if (world == null) {
            player.sendMessage(TranslationManager.get("no_spawn_set"));
            return;
        }

        Location spawn = new Location(
                world,
                custom.getDouble("spawn.x"),
                custom.getDouble("spawn.y"),
                custom.getDouble("spawn.z"),
                (float) custom.getDouble("spawn.yaw"),
                (float) custom.getDouble("spawn.pitch")
        );

        // Save current location for /back command
        SimpleSpawn.main.playerCoordinates.put(
                player.getUniqueId().toString(),
                player.getLocation()
        );

        // Perform teleport
        player.teleport(spawn);
        player.sendMessage(TranslationManager.get("teleport_spawn_success"));
        player.sendActionBar(Component.text(
                TranslationManager.get("actionbar_spawn_success")
        ));
    }
}