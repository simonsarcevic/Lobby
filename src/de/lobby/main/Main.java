package de.lobby.main;

import de.lobby.main.commands.BuildCommand;
import de.lobby.main.events.ConnectionEvents;
import de.lobby.main.events.InteractEvents;
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

        getCommand("build").setExecutor(new BuildCommand());
    }

    private void EventManager(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ConnectionEvents(), this);
        pm.registerEvents(new InventoryEvents(), this);
        pm.registerEvents(new InteractEvents(), this);
    }

    public static Main getInstance() {
        return instance;
    }

}
