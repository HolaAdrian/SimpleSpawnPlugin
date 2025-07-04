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
                commandSender.sendMessage(prefix + ChatColor.RED + "Du hast keine Berechtigung, den Spawn-Punkt zu setzen!");
                return false;
            }
        }
        if (SimpleSpawn.main.getConfig() == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "Es wurde keine config.yml gefunden!");
            return false;
        }
        FileConfiguration config = SimpleSpawn.main.getConfig();
        if (config.getString("prefix") == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "Der Prefix ist beschädigt!");
            return false;
        }
        if (UtilityTools.getCustomConfig() == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "Die spawn.yml Datei wurde nicht gefunden!");
            return false;
        }
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(prefix + ChatColor.RED + "Du musst ein Spieler sein, um den Spawn-Punkt zu setzen!");
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

        player.sendMessage(prefix + ChatColor.GREEN + "Der Spawn-Punkt wurde erfolgreich gesetzt und gespeichert!");

        return true;
    }
}