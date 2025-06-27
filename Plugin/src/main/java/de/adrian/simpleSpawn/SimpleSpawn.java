package de.adrian.simpleSpawn;

import de.adrian.simpleSpawn.Utility.Importer;
import de.adrian.simpleSpawn.Utility.UtilityTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SimpleSpawn extends JavaPlugin {

    public static SimpleSpawn main;

    public String prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));


    @Override
    public void onEnable() {
        main = this;
        if (!getConfig().isSet("prefix")){
            saveDefaultConfig();
        }


        Importer.ImportCommands(main);
        Importer.ImportPermission(Bukkit.getPluginManager());

        UtilityTools.loadCustomConfig();


    }








    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
