package de.adrian.simpleSpawn.Commands;

import de.adrian.simpleSpawn.SimpleSpawn;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import jdk.jshell.execution.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        String prefix = SimpleSpawn.main.prefix;
        if (commandSender instanceof Player){
            if (!commandSender.hasPermission(new Permission("simplespawn.setspawn"))){
                commandSender.sendMessage(prefix + ChatColor.RED+ "You don't have permission to set the spawn!");
                return false;
            }
        }
        if (SimpleSpawn.main.getConfig() == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "No config.yml was found!");
            return false;
        }
        FileConfiguration config = SimpleSpawn.main.getConfig();
        if (config.getString("prefix") == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "The prefix is corrupted!");
            return false;
        }
        if (UtilityTools.getCustomConfig() == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "The spawn.yml file was not found!");
            return false;
        }
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(prefix + ChatColor.RED + "You must be a player to set the spawn!");
            return false;
        }

        Player player = ((Player) commandSender).getPlayer();
        Location location = player.getLocation();

        FileConfiguration custom = UtilityTools.getCustomConfig();

        custom.set("spawn.x", location.getX());
        custom.set("spawn.y", location.getY());
        custom.set("spawn.z", location.getZ());
        custom.set("spawn.yaw", location.getYaw());
        custom.set("spawn.pitch", location.getPitch());
        custom.set("spawn.world", player.getWorld().getName());

        try {
            UtilityTools.getCustomConfig().save(UtilityTools.getCustomConfigFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(prefix + ChatColor.GREEN + "The spawn has been set and saved!");

        return true;

    }
}
