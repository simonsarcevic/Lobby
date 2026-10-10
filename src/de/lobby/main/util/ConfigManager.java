package de.lobby.main.util;


import de.lobby.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private File file;
    private FileConfiguration spawnConfig;

    public void createSpawnFile(){
        File dataFolder = Main.getInstance().getDataFolder();

        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new IllegalStateException("Der Plugin-Ordner konnte nicht erstellt werden: " + dataFolder);
        }

        file = new File(dataFolder, "spawn.yml");

        // Eine ältere Version hat spawn.yml versehentlich als Ordner erstellt.
        // Leere fehlerhafte Ordner werden einmalig repariert.
        if (file.isDirectory()) {
            String[] children = file.list();
            if (children == null || children.length != 0 || !file.delete()) {
                throw new IllegalStateException("spawn.yml ist ein Ordner und konnte nicht repariert werden: " + file);
            }
        }

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new IOException("Die Datei konnte nicht erstellt werden.");
                }
            } catch (IOException exception) {
                throw new IllegalStateException("spawn.yml konnte nicht erstellt werden.", exception);
            }
        }

        spawnConfig = YamlConfiguration.loadConfiguration(file);
    }

    public void saveSpawnFile(){
        try {
            spawnConfig.save(file);
        } catch (IOException exception) {
            throw new IllegalStateException("spawn.yml konnte nicht gespeichert werden.", exception);
        }
    }

    public void setSpawn(Location location){
        setLocation("spawn", location);
    }

    public void setLocation(String name, Location location) {
        String path = name.toLowerCase();
        spawnConfig.set(path + ".world", location.getWorld().getName());
        spawnConfig.set(path + ".x", location.getX());
        spawnConfig.set(path + ".y", location.getY());
        spawnConfig.set(path + ".z", location.getZ());
        spawnConfig.set(path + ".yaw", location.getYaw());
        spawnConfig.set(path + ".pitch", location.getPitch());

        saveSpawnFile();
    }

    public Location getSpawn() {
        return getLocation("spawn");
    }

    public Location getLocation(String name) {
        String path = name.toLowerCase();

        if (!spawnConfig.contains(path + ".world")) {
            return null;
        }

        World world = Bukkit.getWorld(spawnConfig.getString(path + ".world"));

        if (world == null) {
            return null;
        }

        double x = spawnConfig.getDouble(path + ".x");
        double y = spawnConfig.getDouble(path + ".y");
        double z = spawnConfig.getDouble(path + ".z");

        float yaw = (float) spawnConfig.getDouble(path + ".yaw");
        float pitch = (float) spawnConfig.getDouble(path + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }
}
