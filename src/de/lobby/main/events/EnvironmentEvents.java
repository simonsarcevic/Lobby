package de.lobby.main.events;

import de.lobby.main.structure.Locations;
import de.lobby.main.structure.Messages;
import de.lobby.main.util.ConfigManager;
import de.lobby.main.util.JumpAndRunUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class EnvironmentEvents implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        if (event.toWeatherState()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        ConfigManager manager = new ConfigManager();
        Location location = player.getLocation();

        if (location.getY() < 5){
            if (JumpAndRunUtils.isInJumpAndRun(player)){
                player.teleport(manager.getLocation(Locations.JUMP_AND_RUN));
                player.setFallDistance(0);
                player.playSound(location, Sound.NOTE_PLING, 2, 1);
            }else{
                player.teleport(manager.getSpawn());
                player.setFallDistance(0);
                player.playSound(location, Sound.NOTE_PLING, 2, 1);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event){
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        ConfigManager manager = new ConfigManager();
        Location plate = manager.getLocation(Locations.JUMP_AND_RUN);

        if (plate == null) {
            player.sendMessage(Messages.prefix + "§7JumpAndRun Location §cnicht §7gesetzt!");
            return;
        }

        if (!block.getWorld().equals(plate.getWorld())) {
            return;
        }

        if (block.getX() != plate.getBlockX()
                || block.getY() != plate.getBlockY()
                || block.getZ() != plate.getBlockZ()) {
            return;
        }

        if (!JumpAndRunUtils.isInJumpAndRun(player)) {
            JumpAndRunUtils.startJumpAndRun(player);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2, 1);
            player.sendMessage(Messages.prefix + "§7JumpAndRun §agestartet§7!");
        }
    }
}
