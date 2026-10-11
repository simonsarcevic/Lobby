package de.lobby.main.events;

import de.lobby.main.util.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class EnvironmentEvents implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        ConfigManager manager = new ConfigManager();

        Location location = player.getLocation();

        if (location.getY() < 5){
            player.teleport(manager.getSpawn());
            player.setFallDistance(0);
        }
    }
}
