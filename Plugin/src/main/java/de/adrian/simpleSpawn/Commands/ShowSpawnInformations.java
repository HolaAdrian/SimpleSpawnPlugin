package de.adrian.simpleSpawn.Commands;

import de.adrian.simpleSpawn.SimpleSpawn;
import de.adrian.simpleSpawn.Utility.TranslationManager;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ShowSpawnInformations implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        // Check if sender is player
        if (!(sender instanceof Player)) {
            sender.sendMessage(TranslationManager.get("must_be_player_showspawn"));
            return false;
        }

        Player player = (Player) sender;

        // Permission check
        if (!player.hasPermission("simplespawn.showspawn")) {
            player.sendMessage(TranslationManager.get("no_permission_showspawn"));
            return false;
        }

        // Config validation
        if (SimpleSpawn.main.getConfig() == null) {
            player.sendMessage(TranslationManager.get("no_config"));
            return false;
        }

        if (UtilityTools.getCustomConfig() == null) {
            player.sendMessage(TranslationManager.get("no_spawn_file"));
            return false;
        }

        FileConfiguration custom = UtilityTools.getCustomConfig();

        // Spawn location validation
        if (!custom.isSet("spawn.world")) {
            player.sendMessage(TranslationManager.get("no_spawn_set"));
            return false;
        }

        // Get spawn details
        World world = Bukkit.getWorld(custom.getString("spawn.world"));
        if (world == null) {
            player.sendMessage(TranslationManager.get("no_spawn_set"));
            return false;
        }

        // Create replacement map for placeholders
        Map<String, String> replacements = new HashMap<>();
        replacements.put("%x%", String.valueOf(Math.round(custom.getDouble("spawn.x"))));
        replacements.put("%y%", String.valueOf(Math.round(custom.getDouble("spawn.y"))));
        replacements.put("%z%", String.valueOf(Math.round(custom.getDouble("spawn.z"))));
        replacements.put("%yaw%", String.valueOf(Math.round(custom.getDouble("spawn.yaw"))));
        replacements.put("%pitch%", String.valueOf(Math.round(custom.getDouble("spawn.pitch"))));
        replacements.put("%world%", world.getName());

        // Send formatted spawn info
        player.sendMessage(TranslationManager.get("spawn_info", replacements));

        return true;
    }
}