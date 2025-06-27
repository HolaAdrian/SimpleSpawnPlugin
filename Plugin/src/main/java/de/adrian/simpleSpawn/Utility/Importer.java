package de.adrian.simpleSpawn.Utility;

import de.adrian.simpleSpawn.Commands.SetSpawnCommand;
import de.adrian.simpleSpawn.Commands.ShowSpawnInformations;
import de.adrian.simpleSpawn.Commands.SpawnCommand;
import de.adrian.simpleSpawn.SimpleSpawn;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public class Importer {

    public static void ImportPermission(PluginManager pluginManager){
        Permission setspawn = new Permission("simplespawn.setspawn");
        setspawn.setDescription("Allows the player to set the spawn");

        Permission spawn = new Permission("simplespawn.spawn");
        spawn.setDescription("Allows the player to teleport to spawn");

        Permission showspawn = new Permission("simplespawn.showspawn");
        spawn.setDescription("Allows the player to see the spawn coordination's");

        pluginManager.addPermission(setspawn);
        pluginManager.addPermission(spawn);
        pluginManager.addPermission(showspawn);

    }


    public static void ImportCommands(SimpleSpawn main){
        main.getCommand("spawn").setExecutor(new SpawnCommand());
        main.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        main.getCommand("spawninfos").setExecutor(new ShowSpawnInformations());
    }



}
