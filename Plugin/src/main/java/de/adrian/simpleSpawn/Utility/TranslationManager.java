package de.adrian.simpleSpawn.Utility;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TranslationManager {
    private static YamlConfiguration currentLanguage;
    private static String selectedLanguage;
    private static JavaPlugin plugin;
    private static final Map<String, YamlConfiguration> loadedLanguages = new HashMap<>();

    public static void setup(JavaPlugin plugin) {
        TranslationManager.plugin = plugin;
        loadLanguages();
        setLanguage(plugin.getConfig().getString("language", "en"));
    }

    private static void loadLanguages() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        // Supported languages
        String[] languages = {"en", "de", "cu"};

        for (String lang : languages) {
            File langFile = new File(langFolder, lang + ".yml");

            // Save default if doesn't exist
            if (!langFile.exists()) {
                plugin.saveResource("lang/" + lang + ".yml", false);
            }

            // Load the file
            YamlConfiguration config = YamlConfiguration.loadConfiguration(langFile);
            loadedLanguages.put(lang, config);
            plugin.getLogger().info("Loaded language: " + lang);
        }
    }

    public static void setLanguage(String language) {
        if (loadedLanguages.containsKey(language)) {
            selectedLanguage = language;
            currentLanguage = loadedLanguages.get(language);
            plugin.getLogger().info("Set language to: " + language);
        } else {
            plugin.getLogger().warning("Attempted to set invalid language: " + language);
        }
    }

    public static String get(String key) {
        String message = currentLanguage.getString(key);
        if (message == null) {
            plugin.getLogger().warning("Missing translation for key: " + key);
            return ChatColor.RED + "[Missing: " + key + "]";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String get(String key, Object... replacements) {
        String message = get(key);
        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", replacements[i].toString());
        }
        return message;
    }

    public static void reload() {
        loadedLanguages.clear();
        loadLanguages();
        setLanguage(selectedLanguage);
    }
}