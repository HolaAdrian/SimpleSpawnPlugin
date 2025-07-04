package de.adrian.simpleSpawn.Commands;

import de.adrian.simpleSpawn.SimpleSpawn;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private final String prefix = SimpleSpawn.main.prefix;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(prefix + ChatColor.RED + "Du musst ein Spieler sein, um dich zum Spawn zu teleportieren!");
            return false;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("simplespawn.spawn")) {
            player.sendMessage(prefix + ChatColor.RED + "Du hast keine Berechtigung, dich zum Spawn zu teleportieren!");
            return false;
        }

        if (SimpleSpawn.main.getConfig() == null) {
            player.sendMessage(prefix + ChatColor.RED + "Es wurde keine config.yml gefunden!");
            return false;
        }

        FileConfiguration config = SimpleSpawn.main.getConfig();

        if (config.getString("prefix") == null) {
            player.sendMessage(prefix + ChatColor.RED + "Der Prefix ist beschädigt!");
            return false;
        }

        if (UtilityTools.getCustomConfig() == null) {
            player.sendMessage(prefix + ChatColor.RED + "spawn.yml wurde nicht gefunden!");
            return false;
        }

        FileConfiguration custom = UtilityTools.getCustomConfig();

        if (!custom.isSet("spawn.world")) {
            player.sendMessage(prefix + ChatColor.RED + "Es wurde kein Spawn gesetzt oder er ist beschädigt!");
            return false;
        }

        if (!config.isBoolean("ground")) {
            player.sendMessage(prefix + ChatColor.RED + "Die 'ground'-Einstellung in der config.yml ist beschädigt oder nicht gesetzt!");
            return false;
        }

        boolean mustGround = config.getBoolean("ground");

        if (mustGround && !player.isOnGround()) {
            player.sendMessage(prefix + ChatColor.RED + "Du musst am Boden stehen, um dich zum Spawn zu teleportieren!");
            return false;
        }

        if (teleportTask != null) {
            teleportTask.cancel();
        }

        countdowntime = 0;
        teleportToSpawn(player);
        return true;
    }

    private void teleportToSpawn(Player player) {
        FileConfiguration config = SimpleSpawn.main.getConfig();

        if (!config.isBoolean("cooldownrequirement")) {
            player.sendMessage(prefix + ChatColor.RED + "Die 'cooldownrequirement'-Einstellung in der config.yml ist beschädigt oder nicht gesetzt!");
            return;
        }

        if (!config.getBoolean("cooldownrequirement")) {
            teleportPlayer(player);
            return;
        }

        if (!config.isInt("cooldown")) {
            player.sendMessage(prefix + ChatColor.RED + "Die 'cooldown'-Einstellung in der config.yml ist beschädigt oder nicht gesetzt!");
            return;
        }

        int cooldown = config.getInt("cooldown");

        Location loc = player.getLocation();

        teleportTask = Bukkit.getScheduler().runTaskTimer(SimpleSpawn.main, new Runnable() {
            @Override
            public void run() {
                if (countdowntime >= cooldown) {
                    teleportPlayer(player);
                    teleportTask.cancel();
                    return;
                }

                if (Math.round(loc.x()) != Math.round(player.getLocation().x()) || Math.round(loc.y()) != Math.round(player.getLocation().y()) || Math.round(loc.z()) != Math.round(player.getLocation().z())){
                    player.sendActionBar(Component.text(ChatColor.RED + "Du hast dich bewegt, daher wurde der Teleport abgebrochen!"));
                    teleportTask.cancel();
                    return;
                }
                countdowntime++;
                int timeLeft = cooldown - countdowntime + 1;
                player.sendActionBar(Component.text(ChatColor.GREEN + "Teleport in " + timeLeft + " Sekunden..."));
            }
        }, 0L, 20L);
    }

    private void teleportPlayer(Player player) {
        FileConfiguration custom = UtilityTools.getCustomConfig();

        double x = custom.getDouble("spawn.x");
        double y = custom.getDouble("spawn.y");
        double z = custom.getDouble("spawn.z");
        float yaw = (float) custom.getDouble("spawn.yaw");
        float pitch = (float) custom.getDouble("spawn.pitch");
        World world = Bukkit.getWorld(custom.getString("spawn.world"));

        Location spawn = new Location(world, x, y, z, yaw, pitch);

        HashMap<String, Location> playercoordinations = SimpleSpawn.main.playercoordinations;
        playercoordinations.put(player.getUniqueId().toString(), player.getLocation());

        player.teleport(spawn);

        player.sendMessage(prefix + ChatColor.GREEN + "Du wurdest zum Spawn teleportiert!");
        player.sendActionBar(Component.text(ChatColor.GREEN + "Teleport erfolgreich!"));
    }
}