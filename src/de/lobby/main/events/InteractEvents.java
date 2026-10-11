package de.lobby.main.events;

import de.lobby.main.structure.Locations;
import de.lobby.main.util.BuildModeUtils;
import de.lobby.main.util.ConfigManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvents implements Listener {

    private final ConfigManager configManager;

    public InteractEvents(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        BuildModeUtils buildModeUtils = new BuildModeUtils();
        if (buildModeUtils.isPlayerInBuildMode(event.getPlayer())) {
            return;
        }

        if (isJumpAndRunPressurePlate(event)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        BuildModeUtils buildModeUtils = new BuildModeUtils();
        if (!buildModeUtils.isPlayerInBuildMode(event.getPlayer())
                && !isLobbyNpc(event.getRightClicked())) {
            event.setCancelled(true);
        }
    }

    private boolean isLobbyNpc(Entity entity) {
        return entity instanceof NPC || entity.hasMetadata("NPC");
    }

    private boolean isJumpAndRunPressurePlate(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return false;
        }

        Block block = event.getClickedBlock();
        Location jumpAndRun = configManager.getLocation(Locations.JUMP_AND_RUN);
        if (block == null || jumpAndRun == null || jumpAndRun.getWorld() == null) {
            return false;
        }

        return block.getWorld().equals(jumpAndRun.getWorld())
                && block.getX() == jumpAndRun.getBlockX()
                && block.getY() == jumpAndRun.getBlockY()
                && block.getZ() == jumpAndRun.getBlockZ();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        BuildModeUtils buildModeUtils = new BuildModeUtils();

        if (!buildModeUtils.isPlayerInBuildMode(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        BuildModeUtils buildModeUtils = new BuildModeUtils();

        if (!buildModeUtils.isPlayerInBuildMode(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }
}
