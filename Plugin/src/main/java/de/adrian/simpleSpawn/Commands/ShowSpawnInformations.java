package de.adrian.simpleSpawn.Commands;

import de.adrian.simpleSpawn.SimpleSpawn;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

public class ShowSpawnInformations implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        String prefix = SimpleSpawn.main.prefix;
        if (!commandSender.hasPermission(new Permission("simplespawn.showspawn"))) {
            commandSender.sendMessage(prefix + ChatColor.RED + "Du hast keine Berechtigung, die Spawn-Koordinaten einzusehen!");
            return false;
        }
        if (SimpleSpawn.main.getConfig() == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "Die config.yml Datei wurde nicht gefunden!");
            return false;
        }

        FileConfiguration config = SimpleSpawn.main.getConfig();

        if (config.getString("prefix") == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "Der Prefix ist beschädigt!");
            return false;
        }

        if (UtilityTools.getCustomConfig() == null){
            commandSender.sendMessage(prefix + ChatColor.RED + "spawn.yml wurde nicht gefunden!");
            return false;
        }

        FileConfiguration custom = UtilityTools.getCustomConfig();

        if (!custom.isSet("spawn.world")){
            commandSender.sendMessage(prefix + ChatColor.RED + "Es wurde kein Spawn gesetzt oder er ist beschädigt!");
            return false;
        }

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
        commandSender.sendMessage(prefix + "\n" + ChatColor.GOLD + "Spawn Informationen: \nX: " + roundX + "\nY: " + roundY + "\nZ: " + roundZ + "\nYaw: " + roundYaw + "\nPitch: " + roundPitch + "\nWeltname: "+ worldname);

        return true;
    }
}