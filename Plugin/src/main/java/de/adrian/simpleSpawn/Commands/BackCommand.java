package de.adrian.simpleSpawn.Commands;

import de.adrian.simpleSpawn.SimpleSpawn;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class BackCommand implements CommandExecutor {

    private BukkitTask teleportTask;
    private int countdowntime = 0;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        String prefix = SimpleSpawn.main.prefix;
        if (commandSender instanceof Player) {
            if (!commandSender.hasPermission(new Permission("simplespawn.back"))) {
                commandSender.sendMessage(prefix + ChatColor.RED + "You don't have permission to go back!");
                return false;
            }
        }

        if (SimpleSpawn.main.getConfig() == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "No config.yml was found!");
            return false;
        }
        FileConfiguration config = SimpleSpawn.main.getConfig();
        if (config.getString("prefix") == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "The prefix is corrupt!");
            return false;
        }
        if (UtilityTools.getPlayerConfig() == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "The playercoords.yml file was not found!");
            return false;
        }
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(prefix + ChatColor.RED + "You must be a player to go back!");
            return false;
        }

        Player player = ((Player) commandSender).getPlayer();

        if (!SimpleSpawn.main.playercoordinations.containsKey(player.getUniqueId().toString())){
            player.sendMessage(prefix + ChatColor.RED + "No valid coordinates found! Maybe you need to use /spawn first.");
            return false;
        }

        Location loc = SimpleSpawn.main.playercoordinations.get(player.getUniqueId().toString());



        if (!config.isBoolean("ground_back")) {
            player.sendMessage(prefix + ChatColor.RED + "The 'ground_back' setting in config.yml is corrupt or not set");
            return false;
        }


        if (config.getBoolean("ground_back") && !player.isOnGround()){
            player.sendMessage(prefix+ ChatColor.RED + "You must be on the ground to teleport back!");
            return false;
        }

        if (!config.isBoolean("cooldownrequirement_back")) {
            player.sendMessage(prefix + ChatColor.RED + "The 'cooldownrequirement_back' setting in config.yml is corrupt or not set");
            return false;
        }

        if (!config.isBoolean("OneTimeUse")) {
            player.sendMessage(prefix + ChatColor.RED + "The 'OneTimeUse' setting in config.yml is corrupt or not set");
            return false;
        }

        if (!config.getBoolean("cooldownrequirement_back")){
            player.teleport(loc);
            player.sendMessage(prefix + ChatColor.GREEN + "You were teleported back!");
            player.sendActionBar(Component.text(ChatColor.GREEN + "Teleport successful!"));

            if (config.getBoolean("OneTimeUse")) SimpleSpawn.main.playercoordinations.remove(player.getUniqueId().toString());

            return true;
        }

        if (!config.isInt("cooldown_back")){
            player.sendMessage(prefix + ChatColor.RED + "The 'cooldown_back' setting in config.yml is corrupted or not set");
            return false;
        }


        int cooldown = config.getInt("cooldown_back");

        Location loc2 = player.getLocation();

        teleportTask = Bukkit.getScheduler().runTaskTimer(SimpleSpawn.main, new Runnable() {
            @Override
            public void run() {
                if (countdowntime >= cooldown) {
                    player.teleport(loc);
                    player.sendMessage(prefix + ChatColor.GREEN + "You were teleported back!");
                    player.sendActionBar(Component.text(ChatColor.GREEN + "Teleport successful!"));
                    if (config.getBoolean("OneTimeUse")) SimpleSpawn.main.playercoordinations.remove(player.getUniqueId().toString());
                    teleportTask.cancel();
                    return;
                }

                if (Math.round(loc2.x()) != Math.round(player.getLocation().x()) || Math.round(loc2.y()) != Math.round(player.getLocation().y()) || Math.round(loc2.z()) != Math.round(player.getLocation().z())){
                    player.sendActionBar(Component.text(ChatColor.RED + "You moved, so the teleport was canceled!"));
                    teleportTask.cancel();
                    return;
                }
                countdowntime++;
                int timeLeft = cooldown - countdowntime + 1;
                player.sendActionBar(Component.text(ChatColor.GREEN + "Teleporting in " + timeLeft + " seconds..."));


            }
        }, 0L, 20L);

        return true;
    }
}
