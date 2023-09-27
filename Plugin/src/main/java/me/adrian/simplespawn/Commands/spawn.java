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
import org.bukkit.entity.Player;

public class spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (SimpleSpawn.main.getConfig() != null){
            FileConfiguration config = SimpleSpawn.main.getConfig();
            if (config.getString("prefix") != null){
                String prefix = config.getString("prefix");
                if (SimpleSpawn.main.getCustomConfig() != null){
                    FileConfiguration custom = SimpleSpawn.main.getCustomConfig();
                    if (custom.isSet("spawn.world")){
                        double x = custom.getDouble("spawn.x");
                        double y = custom.getDouble("spawn.y");
                        double z = custom.getDouble("spawn.z");
                        Float yaw = (float) custom.getDouble("spawn.yaw");
                        Float pitch = (float) custom.getDouble("spawn.pitch");
                        World world = Bukkit.getWorld(custom.getString("spawn.world"));

                        Location spawn = new Location(world, x, y, z, yaw, pitch);


                        if (config.isBoolean("mustbeonground") == true){
                            boolean mustground = config.getBoolean("mustbeonground");

                            if (commandSender instanceof Player){
                                Player player = ((Player) commandSender).getPlayer();
                                if (mustground == true){
                                    if (player.isOnGround()){
                                        player.teleport(spawn);
                                        player.sendMessage(prefix + ChatColor.GREEN + "You were teleportet to the spawn!");
                                    }
                                    else {
                                        player.sendMessage(prefix + ChatColor.RED + "You must be on ground to teleport to spawn");
                                    }
                                }
                                else if (mustground == false){
                                    player.teleport(spawn);
                                    player.sendMessage(prefix + ChatColor.GREEN + "You were teleportet to the spawn!");
                                }
                            }
                            else {
                                commandSender.sendMessage(prefix + ChatColor.RED + "You must be a player to teleport to spawn!");
                            }


                        }
                        else {
                            commandSender.sendMessage(prefix + ChatColor.RED + "mustbeonground in config.yml is corrupted or not set");
                        }

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
                commandSender.sendMessage(ChatColor.RED + "The prefix is empty or corrupted!");
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "No config.yml was found this might be a bug!");
        }




        return false;
    }
}
