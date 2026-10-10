package de.lobby.main;

import de.lobby.main.events.ConnectionEvents;
import de.lobby.main.events.InventoryEvents;
import de.lobby.main.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager manager = new ConfigManager();
        EventManager();
        manager.createConfig();
        manager.saveConfig();
        manager.loadConfig();
    }

    private void EventManager(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ConnectionEvents(), this);
        pm.registerEvents(new InventoryEvents(), this);
    }

    public static Main getInstance() {
        return instance;
    }

}
