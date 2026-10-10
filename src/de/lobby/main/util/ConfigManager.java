package de.lobby.main.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private final File file = new File("plugins/Lobby", "config.yml");
    private final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public void saveConfig(){
        try {
            cfg.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void addToConfig(String path, Object properties){
        try {
            cfg.set(path, properties);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        saveConfig();
    }

    public void getFromConfig(String path){
        try {
            cfg.get(path);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void loadConfig(){
        try {
            cfg.load(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void createConfig(){
        try {
            if (!file.exists()){
                file.mkdirs();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
