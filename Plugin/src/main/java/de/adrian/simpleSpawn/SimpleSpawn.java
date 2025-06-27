package de.adrian.simpleSpawn;

import de.adrian.simpleSpawn.Utility.Importer;
import de.adrian.simpleSpawn.Utility.SafeManager;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public final class SimpleSpawn extends JavaPlugin {

    public static SimpleSpawn main;

    public String prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));

    public HashMap<String, Location> playercoordinations = new HashMap<>();


    @Override
    public void onEnable() {
        main = this;
        if (!getConfig().isSet("prefix")){
            saveDefaultConfig();
        }


        Importer.ImportCommands(main);
        Importer.ImportPermission(Bukkit.getPluginManager());

        UtilityTools.loadCustomConfig();
        UtilityTools.loadPlayerConfig();

        SafeManager.loadAll();


    }








    @Override
    public void onDisable() {
        SafeManager.saveAll();
    }
}
