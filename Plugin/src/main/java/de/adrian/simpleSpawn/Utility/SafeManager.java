package de.adrian.simpleSpawn.Utility;

import de.adrian.simpleSpawn.SimpleSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SafeManager {


    private static FileConfiguration playerConfig = UtilityTools.getPlayerConfig();
    private static File playerConfigFile = UtilityTools.getPlayerConfigFile();

    public static void saveAll() {


        // Clear existing data
        playerConfig.set("player-spawns", null);

        // Save all entries from HashMap
        for (Map.Entry<String, Location> entry : SimpleSpawn.main.playercoordinations.entrySet()) {
            String uuidString = entry.getKey();
            Location loc = entry.getValue();

            String path = "player-spawns." + uuidString;
            playerConfig.set(path + ".x", loc.getX());
            playerConfig.set(path + ".y", loc.getY());
            playerConfig.set(path + ".z", loc.getZ());
            playerConfig.set(path + ".yaw", loc.getYaw());
            playerConfig.set(path + ".pitch", loc.getPitch());
            playerConfig.set(path + ".world", loc.getWorld().getName());
        }

        savePlayerConfig();
    }

    // Load ALL locations from YAML into memory
    public static void loadAll() {

        SimpleSpawn.main.playercoordinations.clear();

        if (!playerConfig.contains("player-spawns")) {
            return;
        }

        for (String uuidString : playerConfig.getConfigurationSection("player-spawns").getKeys(false)) {
            String path = "player-spawns." + uuidString;
            double x = playerConfig.getDouble(path + ".x");
            double y = playerConfig.getDouble(path + ".y");
            double z = playerConfig.getDouble(path + ".z");
            float yaw = (float) playerConfig.getDouble(path + ".yaw");
            float pitch = (float) playerConfig.getDouble(path + ".pitch");
            World world = Bukkit.getWorld(playerConfig.getString(path + ".world"));

            if (world == null) {
                return;
            }

            Location loc = new Location(world, x, y, z, yaw, pitch);


            if (loc != null) {
                SimpleSpawn.main.playercoordinations.put(uuidString, loc);
            }
        }
    }


    // Internal save method
    private static void savePlayerConfig() {
        try {
            playerConfig.save(playerConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
