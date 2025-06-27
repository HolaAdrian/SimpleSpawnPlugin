package me.adrian.simplespawn.Commands;

import me.adrian.simplespawn.SimpleSpawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;



public class SpawnCoordsCommand implements CommandExecutor {
    String prefix = SimpleSpawn.main.getPrefix();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if (!commandSender.hasPermission(new Permission("simplespawn.showspawn"))) {
                FileConfiguration config = SimpleSpawn.main.getConfig();
                commandSender.sendMessage(prefix + ChatColor.RED + "You don't have permission to see the spawns coordination's!");
                return false;
            }
        if (SimpleSpawn.main.getConfig() != null){
            FileConfiguration config = SimpleSpawn.main.getConfig();
            if (config.getString("prefix") != null){
                if (SimpleSpawn.main.getCustomConfig() != null){
                    FileConfiguration custom = SimpleSpawn.main.getCustomConfig();
                    if (custom.isSet("spawn.world")){
                        double x = custom.getDouble("spawn.x");
                        double y = custom.getDouble("spawn.y");
                        double z = custom.getDouble("spawn.z");
                        Float yaw = (float) custom.getDouble("spawn.yaw");
                        Float pitch = (float) custom.getDouble("spawn.pitch");
                        World world = Bukkit.getWorld(custom.getString("spawn.world"));

                        double roundX = Math.round(x);
                        double roundY = Math.round(y);
                        double roundZ = Math.round(z);
                        double roundYaw = Math.round(yaw);
                        double roundPitch = Math.round(pitch);
                        String worldname = world.getName();
                        commandSender.sendMessage(prefix + ChatColor.GOLD + "Spawn Informations: \nX: " + roundX + "\nY: " + roundY + "\nZ: " + roundZ + "\nYaw: " + roundYaw + "\nPitch: " + roundPitch + "\nWorldname: "+ worldname);
                    }
                    else {
                        commandSender.sendMessage(prefix + ChatColor.RED + "No spawn was set or it's corrupted please contact an admin!");
                    }
                }
                else {
                    commandSender.sendMessage(prefix + ChatColor.RED + "spawn.yml was not found this might be a bug!");
                }
            }
            else {
                commandSender.sendMessage(prefix + ChatColor.RED + "The prefix is empty or corrupted!");
            }
        }
        else {
            FileConfiguration config = SimpleSpawn.main.getConfig();
            commandSender.sendMessage(prefix + ChatColor.RED + "No config.yml was found this might be a bug!");
        }


        return false;
    }
}
