package de.lobby.main.events;

import de.lobby.main.Main;
import de.lobby.main.util.ConfigManager;
import de.lobby.main.util.HidePlayerManager;
import de.lobby.main.util.JumpAndRunUtils;
import de.lobby.main.util.LobbyItems;
import de.lobby.main.util.ServerConnector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionEvents implements Listener {

    private final ConfigManager configManager;

    public ConnectionEvents(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        LobbyItems.getLobbyItems(player);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player) && HidePlayerManager.containsHideList(onlinePlayer)) {
                onlinePlayer.hidePlayer(player);
            }
        }

        Location spawn = configManager.getSpawn();
        if (spawn != null) {
            player.teleport(spawn);
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            if (player.isOnline()) {
                ServerConnector.requestPendingDestination(player);
            }
        }, 10L);
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        HidePlayerManager.clearPlayer(event.getPlayer());
        JumpAndRunUtils.exitJumpAndRun(event.getPlayer());
        event.setQuitMessage(null);
    }
}
