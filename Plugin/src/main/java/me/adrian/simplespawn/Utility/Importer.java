package me.adrian.simplespawn.Utility;

import me.adrian.simplespawn.Commands.SpawnCoordsCommand;
import me.adrian.simplespawn.Commands.setspawn;
import me.adrian.simplespawn.Commands.spawn;
import me.adrian.simplespawn.SimpleSpawn;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public class Importer {


    public static void ImportPermission(PluginManager pluginManager){
        Permission setspawn = new Permission("simplespawn.setspawn");
        setspawn.setDescription("Allowes the player to set the spawn");

        Permission spawn = new Permission("simplespawn.spawn");
        spawn.setDescription("Allowes the player to teleport to spawn");

        Permission showspawn = new Permission("simplespawn.showspawn");
        spawn.setDescription("Allowes the player to see the spawn coordination's");

        pluginManager.addPermission(setspawn);
        pluginManager.addPermission(spawn);
        pluginManager.addPermission(showspawn);

    }



    public static void ImportCommands(SimpleSpawn main){
        main.getCommand("setspawn").setExecutor(new setspawn());
        main.getCommand("spawn").setExecutor(new spawn());
        main.getCommand("spawncoords").setExecutor(new SpawnCoordsCommand());
    }


}
