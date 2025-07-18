package de.adrian.simpleSpawn.Utility;

import de.adrian.simpleSpawn.SimpleSpawn;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class UtilityTools {




    public static FileConfiguration customConfig = null;
    public static File customConfigFile = null;

    public static FileConfiguration getCustomConfig() {
        return customConfig;
    }

    public static File getCustomConfigFile() {
        return customConfigFile;
    }

    public static void loadCustomConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(SimpleSpawn.main.getDataFolder(), "spawn.yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        customConfig.addDefault("spawn.x", "0");
        customConfig.addDefault("spawn.y", "0");
        customConfig.addDefault("spawn.z", "0");
        customConfig.addDefault("spawn.yaw", "0");
        customConfig.addDefault("spawn.pitch", "0");
        customConfig.addDefault("spawn.world", null);



        customConfig.options().copyDefaults(true);

        saveCustomConfig();


    }

    public static void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            customConfig.save(customConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static FileConfiguration playerConfig = null;
    public static File playerConfigFile = null;

    public static FileConfiguration getPlayerConfig() {
        return playerConfig;
    }

    public static File getPlayerConfigFile() {
        return playerConfigFile;
    }

    public static void loadPlayerConfig() {
        if (playerConfigFile == null) {
            playerConfigFile = new File(SimpleSpawn.main.getDataFolder(), "playercoords.yml");
        }
        playerConfig = YamlConfiguration.loadConfiguration(playerConfigFile);



        playerConfig.options().copyDefaults(true);

        savePlayerConfig();


    }

    public static void savePlayerConfig() {
        if (playerConfig == null || playerConfigFile == null) {
            return;
        }
        try {
            playerConfig.save(playerConfigFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
