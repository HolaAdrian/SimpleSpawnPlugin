package de.adrian.simpleSpawn;

import de.adrian.simpleSpawn.Utility.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class SimpleSpawn extends JavaPlugin {

    public static SimpleSpawn main;
    public static String prefix;
    public HashMap<String, Location> playerCoordinates = new HashMap<>();

    @Override
    public void onEnable() {
        main = this;

        // Load configurations
        saveDefaultConfig();
        reloadConfig();

        // Initialize systems
        TranslationManager.setup(this);
        UtilityTools.loadCustomConfig();
        UtilityTools.loadPlayerConfig();
        SafeManager.loadAll();

        // Set prefix from translations
        prefix = ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("prefix", TranslationManager.get("prefix")));

        // Register components
        Importer.ImportCommands(this);
        Importer.ImportPermission(Bukkit.getPluginManager());

        getLogger().info(prefix + "Enabled successfully!");
    }

    @Override
    public void onDisable() {
        SafeManager.saveAll();
        getLogger().info(prefix + "Disabled successfully!");
    }
}