package de.adrian.simpleSpawn.Commands;

import de.adrian.simpleSpawn.SimpleSpawn;
import de.adrian.simpleSpawn.Utility.TranslationManager;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BackCommand implements CommandExecutor {
    private BukkitTask teleportTask;
    private int countdownTime = 0;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        // Validate sender
        if (!(sender instanceof Player)) {
            sender.sendMessage(TranslationManager.get("must_be_player_back"));
            return false;
        }

        Player player = (Player) sender;

        // Permission check
        if (!player.hasPermission("simplespawn.back")) {
            player.sendMessage(TranslationManager.get("no_permission_back"));
            return false;
        }

        // Config validation
        FileConfiguration config = validateConfigs(player);
        if (config == null) return false;

        // Location validation
        Location returnLocation = validateLocation(player);
        if (returnLocation == null) return false;

        // Ground check
        if (!validateGround(player, config)) return false;

        // Cooldown handling
        if (!handleCooldown(player, config, returnLocation)) return false;

        return true;
    }

    private FileConfiguration validateConfigs(Player player) {
        if (SimpleSpawn.main.getConfig() == null) {
            player.sendMessage(TranslationManager.get("no_config"));
            return null;
        }

        if (UtilityTools.getPlayerConfig() == null) {
            player.sendMessage(TranslationManager.get("no_playercoords_file"));
            return null;
        }

        return SimpleSpawn.main.getConfig();
    }

    private Location validateLocation(Player player) {
        String uuidString = player.getUniqueId().toString();
        if (!SimpleSpawn.main.playerCoordinates.containsKey(uuidString)) {
            player.sendMessage(TranslationManager.get("no_coords_back"));
            return null;
        }
        return SimpleSpawn.main.playerCoordinates.get(uuidString);
    }

    private boolean validateGround(Player player, FileConfiguration config) {
        if (!config.isBoolean("ground_back")) {
            sendSettingError(player, "ground_back");
            return false;
        }

        if (config.getBoolean("ground_back") && !player.isOnGround()) {
            player.sendMessage(TranslationManager.get("ground_required_back"));
            return false;
        }
        return true;
    }

    private boolean handleCooldown(Player player, FileConfiguration config, Location returnLocation) {
        // Validate cooldown settings
        if (!config.isBoolean("cooldownrequirement_back")) {
            sendSettingError(player, "cooldownrequirement_back");
            return false;
        }

        if (!config.isBoolean("OneTimeUse")) {
            sendSettingError(player, "OneTimeUse");
            return false;
        }

        // Instant teleport if no cooldown
        if (!config.getBoolean("cooldownrequirement_back")) {
            executeTeleport(player, returnLocation, config.getBoolean("OneTimeUse"));
            return true;
        }

        // Validate cooldown time
        if (!config.isInt("cooldown_back")) {
            player.sendMessage(TranslationManager.get("cooldown_back_corrupt"));
            return false;
        }

        // Start cooldown process
        startTeleportCountdown(player, returnLocation,
                config.getInt("cooldown_back"),
                config.getBoolean("OneTimeUse"));
        return true;
    }

    private void sendSettingError(Player player, String setting) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("%setting%", setting);
        player.sendMessage(TranslationManager.get("setting_corrupt", replacements));
    }

    private void executeTeleport(Player player, Location location, boolean oneTimeUse) {
        player.teleport(location);
        player.sendMessage(TranslationManager.get("teleport_back_success"));
        player.sendActionBar(Component.text(
                TranslationManager.get("actionbar_back_success")
        ));

        if (oneTimeUse) {
            SimpleSpawn.main.playerCoordinates.remove(player.getUniqueId().toString());
        }
    }

    private void startTeleportCountdown(Player player, Location destination,
                                        int cooldown, boolean oneTimeUse) {
        // Cancel existing task if running
        if (teleportTask != null) {
            teleportTask.cancel();
        }

        Location initialLocation = player.getLocation();
        countdownTime = 0;

        teleportTask = Bukkit.getScheduler().runTaskTimer(SimpleSpawn.main, () -> {
            if (countdownTime >= cooldown) {
                executeTeleport(player, destination, oneTimeUse);
                teleportTask.cancel();
                return;
            }

            // Check for movement
            if (hasMoved(player, initialLocation)) {
                player.sendActionBar(Component.text(
                        TranslationManager.get("cooldown_back_canceled")
                ));
                teleportTask.cancel();
                return;
            }

            countdownTime++;
            int timeLeft = cooldown - countdownTime + 1;

            Map<String, String> replacements = new HashMap<>();
            replacements.put("%seconds%", String.valueOf(timeLeft));
            player.sendActionBar(Component.text(
                    TranslationManager.get("cooldown_back_start", replacements)
            ));
        }, 0L, 20L);
    }

    private boolean hasMoved(Player player, Location initialLocation) {
        Location current = player.getLocation();
        return Math.round(initialLocation.getX()) != Math.round(current.getX()) ||
                Math.round(initialLocation.getY()) != Math.round(current.getY()) ||
                Math.round(initialLocation.getZ()) != Math.round(current.getZ());
    }
}