package de.lobby.main.events;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DoubleJump implements Listener {

    private final Set<UUID> usedDoubleJump = new HashSet<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        usedDoubleJump.remove(player.getUniqueId());
        if (hasNativeFlight(player)) return;
        player.setAllowFlight(true);
    }


    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event){
        Player player = event.getPlayer();

        if (hasNativeFlight(player)) return;

        event.setCancelled(true);
        player.setFlying(false);
        player.setAllowFlight(false);

        if (!usedDoubleJump.add(player.getUniqueId())) {
            return;
        }

        Vector vector = player.getLocation().getDirection().multiply(3).setY(1);
        player.setVelocity(vector);
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 2, 1);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if (!hasNativeFlight(player) && hasLanded(event)){
            usedDoubleJump.remove(player.getUniqueId());
            player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        usedDoubleJump.remove(event.getPlayer().getUniqueId());
    }

    private boolean hasNativeFlight(Player player) {
        return player.getGameMode() == GameMode.CREATIVE
                || player.getGameMode() == GameMode.SPECTATOR;
    }

    private boolean hasLanded(PlayerMoveEvent event) {
        Location to = event.getTo();
        if (to == null || to.getY() > event.getFrom().getY()) {
            return false;
        }

        return to.clone().subtract(0.0D, 0.15D, 0.0D).getBlock().getType() != Material.AIR;
    }
}
