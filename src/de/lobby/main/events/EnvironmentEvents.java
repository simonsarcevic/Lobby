package de.lobby.main.events;

import de.lobby.main.structure.Locations;
import de.lobby.main.structure.Messages;
import de.lobby.main.util.ConfigManager;
import de.lobby.main.util.DailyRewardInventory;
import de.lobby.main.util.DailyRewardManager;
import de.lobby.main.util.JumpAndRunUtils;
import org.bukkit.Location;
import org.bukkit.Material;
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

    private final ConfigManager configManager;
    private final DailyRewardManager dailyRewardManager;

    public EnvironmentEvents(ConfigManager configManager, DailyRewardManager dailyRewardManager) {
        this.configManager = configManager;
        this.dailyRewardManager = dailyRewardManager;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        if (event.toWeatherState()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (location.getY() < 7){
            Location target;
            if (JumpAndRunUtils.isInJumpAndRun(player)){
                target = configManager.getLocation(Locations.JUMP_AND_RUN);
            }else{
                target = configManager.getSpawn();
            }

            if (target == null) {
                return;
            }

            player.teleport(target);
            player.setFallDistance(0);
            player.playSound(target, Sound.NOTE_PLING, 2, 1);
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

        Location plate = configManager.getLocation(Locations.JUMP_AND_RUN);

        if (plate == null) {
            player.sendMessage(Messages.prefix + Messages.jumpAndRunLocationNotSet);
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
            player.sendMessage(Messages.prefix + Messages.jumpAndRunStarted);
        }
    }

    @EventHandler(ignoreCancelled = false)
    public void onRewardInteract(PlayerInteractEvent event){
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        Location chest = configManager.getLocation(Locations.REWARD_CHEST);
        if (block == null || chest == null || chest.getWorld() == null) {
            return;
        }

        if (!block.getWorld().equals(chest.getWorld())
                || block.getX() != chest.getBlockX()
                || block.getY() != chest.getBlockY()
                || block.getZ() != chest.getBlockZ()) {
            return;
        }

        if (block.getType() != Material.CHEST && block.getType() != Material.TRAPPED_CHEST) {
            return;
        }

        Player player = event.getPlayer();
        event.setCancelled(true);
        player.openInventory(DailyRewardInventory.create(player, dailyRewardManager));
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2, 1);
    }
}
