package de.lobby.main;

import de.lobby.main.commands.*;
import de.lobby.main.events.*;
import de.lobby.main.structure.Inventories;
import de.lobby.main.structure.Items;
import de.lobby.main.structure.Locations;
import de.lobby.main.util.ConfigManager;
import de.lobby.main.util.CoinSystemBridge;
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
    private CoinSystemBridge coinSystemBridge;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();
        configManager.createSpawnFile();
        dailyRewardManager = new DailyRewardManager(this);
        try {
            coinSystemBridge = new CoinSystemBridge();
        } catch (IllegalStateException exception) {
            getLogger().warning(exception.getMessage()
                    + " Daily Rewards werden deaktiviert; die Lobby bleibt aktiv.");
        }
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
        getCommand("lobby").setExecutor(new ReturnToLobbyCommand(configManager));
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
        pm.registerEvents(new InventoryEvents(
                this, dailyRewardManager, coinSystemBridge
        ), this);
        pm.registerEvents(new InteractEvents(configManager), this);
        pm.registerEvents(new EnvironmentEvents(configManager, dailyRewardManager), this);
        pm.registerEvents(new DoubleJump(), this);
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CoinSystemBridge getCoinSystemBridge() {
        return coinSystemBridge;
    }
}
