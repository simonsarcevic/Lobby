package de.lobby.main;

import de.einkaktus.coins.api.CoinSystemAPI;
import de.lobby.main.commands.*;
import de.lobby.main.events.ConnectionEvents;
import de.lobby.main.events.EnvironmentEvents;
import de.lobby.main.events.InteractEvents;
import de.lobby.main.events.InventoryEvents;
import de.lobby.main.structure.Inventories;
import de.lobby.main.structure.Items;
import de.lobby.main.structure.Locations;
import de.lobby.main.util.ConfigManager;
import de.lobby.main.util.DailyRewardManager;
import de.lobby.main.util.NavigationMessageListener;
import de.lobby.main.util.ServerConnector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private ConfigManager configManager;
    private DailyRewardManager dailyRewardManager;
    private CoinSystemAPI coinAPI;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();
        configManager.createSpawnFile();
        dailyRewardManager = new DailyRewardManager(this);
        coinAPI = CoinSystemAPI.get();
        EventManager();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(this, ServerConnector.NAVIGATION_CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel(
                this,
                ServerConnector.NAVIGATION_CHANNEL,
                new NavigationMessageListener(configManager)
        );

        for (int slot = 0; slot < Inventories.MENU_INVENTORY.getSize(); slot++) {
            Inventories.MENU_INVENTORY.setItem(slot, Items.navigatorFiller.clone());
        }
        Inventories.MENU_INVENTORY.setItem(4, Items.dailyRewards);
        Inventories.MENU_INVENTORY.setItem(20, Items.training);
        Inventories.MENU_INVENTORY.setItem(22, Items.spawn);
        Inventories.MENU_INVENTORY.setItem(24, Items.kitpvp);

        for (int slot = 0; slot < Inventories.LOBBY_SWITCHER.getSize(); slot++) {
            Inventories.LOBBY_SWITCHER.setItem(slot, Items.lobbySwitcherFiller.clone());
        }
        Inventories.LOBBY_SWITCHER.setItem(11, Items.lobby);
        Inventories.LOBBY_SWITCHER.setItem(13, Items.lobby1);
        Inventories.LOBBY_SWITCHER.setItem(15, Items.lobby2);

        getCommand("build").setExecutor(new BuildCommand());
        getCommand("setlobby").setExecutor(new LobbyCommand(configManager));
        getCommand("settraining").setExecutor(new SetLocationCommand(
                configManager, Locations.TRAINING, "Training", "lobby.settraining"
        ));
        getCommand("setkitpvp").setExecutor(new SetLocationCommand(
                configManager, Locations.KITPVP, "KitPvP", "lobby.setkitpvp"
        ));
        getCommand("setdailychests").setExecutor(new SetLocationCommand(
                configManager, Locations.DAILY_CHESTS, "Daily Chests", "lobby.setdailychests"
        ));
        getCommand("setjumpandrun").setExecutor(new SetJumpAndRunStartCommand(
                configManager, Locations.JUMP_AND_RUN, "lobby.admin"
        ));

        getCommand("setrewardchest").setExecutor(new SetRewardChestCommand(
                configManager, Locations.REWARD_CHEST, "lobby.setrewardchest"
        ));
    }

    private void EventManager(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ConnectionEvents(configManager), this);
        pm.registerEvents(new InventoryEvents(this, configManager, dailyRewardManager, coinAPI), this);
        pm.registerEvents(new InteractEvents(configManager), this);
        pm.registerEvents(new EnvironmentEvents(configManager, dailyRewardManager), this);
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CoinSystemAPI getCoinAPI() {
        return coinAPI;
    }
}
