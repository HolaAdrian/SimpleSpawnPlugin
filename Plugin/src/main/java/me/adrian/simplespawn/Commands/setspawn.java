package me.adrian.simplespawn.Commands;

import me.adrian.simplespawn.SimpleSpawn;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.io.IOException;

public class setspawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            if (!commandSender.hasPermission(new Permission("simplespawn.setspawn"))){
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', SimpleSpawn.main.getConfig().getString("prefix")) + ChatColor.RED+ "You don't have permission to set the spawn!");
                return false;
            }
        }
        if (SimpleSpawn.main.getConfig() != null){
            FileConfiguration config = SimpleSpawn.main.getConfig();
            if (config.getString("prefix") != null){
                String prefix = config.getString("prefix");
                if (commandSender instanceof Player){
                    Player player = ((Player) commandSender).getPlayer();
                    Location location = player.getLocation();
                    World world = player.getWorld();

                    if (SimpleSpawn.main.getCustomConfig() != null){
                        FileConfiguration custom = SimpleSpawn.main.getCustomConfig();
                        custom.set("spawn.x", location.getX());
                        custom.set("spawn.y", location.getY());
                        custom.set("spawn.z", location.getZ());
                        custom.set("spawn.yaw", location.getYaw());
                        custom.set("spawn.pitch", location.getPitch());
                        custom.set("spawn.world", world.getName());

                        try {
                            SimpleSpawn.main.getCustomConfig().save(SimpleSpawn.main.getCustomConfigFile());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        player.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.GREEN + "The spawn was set and saved!");
                    }
                    else {
                        commandSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.RED + "No spawn.yml was found or this is an bug!");
                    }
            }
                else {
                    commandSender.sendMessage((ChatColor.translateAlternateColorCodes('&', prefix)) + ChatColor.RED + "You have to be a player to set the spawn!");
                }


            }
            else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', SimpleSpawn.main.getConfig().getString("prefix")) + ChatColor.RED + "The prefix in the config is corrupted or is a false format!");

            }
        }
        else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', SimpleSpawn.main.getConfig().getString("prefix")) + ChatColor.RED + "There is no config.yml or this is a bug");
        }
        return false;
    }
}
