package de.adrian.simpleSpawn.Commands;

import de.adrian.simpleSpawn.SimpleSpawn;
import de.adrian.simpleSpawn.Utility.TranslationManager;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        // Check if sender is player
        if (!(sender instanceof Player)) {
            sender.sendMessage(TranslationManager.get("must_be_player_setspawn"));
            return false;
        }

        Player player = (Player) sender;

        // Permission check
        if (!player.hasPermission("simplespawn.setspawn")) {
            player.sendMessage(TranslationManager.get("no_permission_setspawn"));
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

        // Save spawn location
        Location location = player.getLocation();
        FileConfiguration custom = UtilityTools.getCustomConfig();

        custom.set("spawn.x", location.getX());
        custom.set("spawn.y", location.getY());
        custom.set("spawn.z", location.getZ());
        custom.set("spawn.yaw", location.getYaw());
        custom.set("spawn.pitch", location.getPitch());
        custom.set("spawn.world", location.getWorld().getName());

        try {
            custom.save(UtilityTools.getCustomConfigFile());
            player.sendMessage(TranslationManager.get("spawn_set_success"));
        } catch (IOException e) {
            player.sendMessage(TranslationManager.get("spawn.save_error"));
            SimpleSpawn.main.getLogger().severe("Failed to save spawn location: " + e.getMessage());
        }

        return true;
    }
}