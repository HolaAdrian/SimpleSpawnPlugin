package me.adrian.simplespawn;

import me.adrian.simplespawn.Utility.Importer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class SimpleSpawn extends JavaPlugin {

    public static SimpleSpawn main;

    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public FileConfiguration getCustomConfig() {
        return customConfig;
    }

    public File getCustomConfigFile() {
        return customConfigFile;
    }

    @Override
    public void onEnable() {
        main = this;
        if (!getConfig().isSet("prefix")){
            saveDefaultConfig();
        }

        Importer.ImportCommands(this);
        Importer.ImportPermission(Bukkit.getPluginManager());

        loadCustomConfig();

    }

    @Override
    public void onDisable() {

    }


    public void loadCustomConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(getDataFolder(), "spawn.yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

       // if (customConfig.isSet("spawn.world")){
            //if (customConfig.get("spawn.world") == null){
                customConfig.addDefault("spawn.x", "0");
                customConfig.addDefault("spawn.y", "0");
                customConfig.addDefault("spawn.z", "0");
                customConfig.addDefault("spawn.yaw", "0");
                customConfig.addDefault("spawn.pitch", "0");
                customConfig.addDefault("spawn.world", null);



                customConfig.options().copyDefaults(true);

                saveCustomConfig();
            //}
        //}


    }

    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            customConfig.save(customConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
