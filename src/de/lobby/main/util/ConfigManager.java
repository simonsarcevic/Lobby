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
        if (!Main.getInstance().getDataFolder().exists()){
            Main.getInstance().getDataFolder().mkdirs();
        }

        file = new File(Main.getInstance().getDataFolder(), "spawn.yml");

        if (!file.mkdirs()){
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        spawnConfig = YamlConfiguration.loadConfiguration(file);
    }

    public void saveSpawnFile(){
        try {
            spawnConfig.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setSpawn(Location location){
        spawnConfig.set("spawn.world", location.getWorld().getName());
        spawnConfig.set("spawn.x", location.getX());
        spawnConfig.set("spawn.y", location.getY());
        spawnConfig.set("spawn.z", location.getZ());
        spawnConfig.set("spawn.yaw", location.getYaw());
        spawnConfig.set("spawn.pitch", location.getPitch());

        saveSpawnFile();
    }

    public Location getSpawn() {
        if (!spawnConfig.contains("spawn.world")) {
            return null;
        }

        World world = Bukkit.getWorld(spawnConfig.getString("spawn.world"));

        if (world == null) {
            return null;
        }

        double x = spawnConfig.getDouble("spawn.x");
        double y = spawnConfig.getDouble("spawn.y");
        double z = spawnConfig.getDouble("spawn.z");

        float yaw = (float) spawnConfig.getDouble("spawn.yaw");
        float pitch = (float) spawnConfig.getDouble("spawn.pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }
}
