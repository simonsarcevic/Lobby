package de.lobby.main.events;

import de.lobby.main.util.ConfigManager;
import de.lobby.main.util.LobbyItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        ConfigManager manager = new ConfigManager();

        LobbyItems.getLobbyItems(player);

        player.teleport(manager.getSpawn());
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
    }
}
