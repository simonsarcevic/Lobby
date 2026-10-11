package de.lobby.main.events;

import de.lobby.main.structure.*;
import de.lobby.main.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InventoryEvents implements Listener {

    private final ConfigManager configManager;

    public InventoryEvents(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onInv(InventoryClickEvent event){
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        BuildModeUtils buildModeUtils = new BuildModeUtils();
        boolean lobbySwitcher = event.getInventory().getTitle().equals(InventoryTitles.LOBBY_SWITCHER);
        boolean navigator = event.getInventory().getTitle().equals(InventoryTitles.NAVIGATOR);

        if (!buildModeUtils.isPlayerInBuildMode(player) || lobbySwitcher || navigator) {
            event.setCancelled(true);
        }

        if (!lobbySwitcher && !navigator) return;

        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

        String itemTitle = event.getCurrentItem().getItemMeta().getDisplayName();

        if (navigator) {
            if (itemTitle.equals(ItemTitles.TRAINING)) {
                teleportTo(player, "training");
            } else if (itemTitle.equals(ItemTitles.KITPVP)) {
                teleportTo(player, "kitpvp");
            } else if (itemTitle.equals(ItemTitles.SPAWN)) {
                teleportTo(player, "spawn");
            } else if (itemTitle.equals(ItemTitles.DAILY_REWARDS)) {
                teleportTo(player, "dailychests");
            }
            return;
        }

        if (itemTitle.equals(ItemTitles.LOBBY)) {
            player.closeInventory();
            ServerConnector.connect(player, "lobby1");
        } else if (itemTitle.equals(ItemTitles.LOBBY_2)) {
            player.closeInventory();
            ServerConnector.connect(player, "lobby2");
        } else if (itemTitle.equals(ItemTitles.LOBBY_3)) {
            player.closeInventory();
            ServerConnector.connect(player, "lobby3");
        }

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        BuildModeUtils buildModeUtils = new BuildModeUtils();

        if (!buildModeUtils.isPlayerInBuildMode(player)
                || event.getInventory().getTitle().equals(InventoryTitles.LOBBY_SWITCHER)
                || event.getInventory().getTitle().equals(InventoryTitles.NAVIGATOR)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        BuildModeUtils buildModeUtils = new BuildModeUtils();

        if (!buildModeUtils.isPlayerInBuildMode(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    private void teleportTo(Player player, String locationName) {
        player.closeInventory();
        Location location = configManager.getLocation(locationName);

        if (location == null) {
            player.sendMessage(Messages.prefix + Messages.locationNotSet);
            return;
        }

        player.teleport(location);
        player.playSound(location, Sound.ENDERMAN_TELEPORT, 1, 1);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if (event.hasItem()){
            if (event.getItem().hasItemMeta()){
                if (event.getItem().getItemMeta().getDisplayName().equals(ItemTitles.NAVIGATOR)) {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 2, 1);
                        player.openInventory(Inventories.MENU_INVENTORY);
                        event.setCancelled(true);
                    }
                }

                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    String itemTitle = event.getItem().getItemMeta().getDisplayName();
                    if (itemTitle.equals(ItemTitles.HIDE_PLAYER) || itemTitle.equals(ItemTitles.SHOW_PLAYER)) {
                        event.setCancelled(true);

                        long remainingSeconds = HidePlayerManager.getRemainingCooldownSeconds(player);
                        if (remainingSeconds > 0) {
                            player.sendMessage(Messages.prefix + Messages.visibilityCooldown.replace(
                                    "%seconds%", String.valueOf(remainingSeconds)
                            ));
                            return;
                        }

                        if (!HidePlayerManager.containsHideList(player)) {
                            HidePlayerManager.setHidePlayer(player);
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (!onlinePlayer.equals(player)) {
                                    player.hidePlayer(onlinePlayer);
                                }
                            }
                            player.getInventory().setItem(4, Items.playerShower.clone());
                            player.sendMessage(Messages.prefix + Messages.hidePlayers);
                        } else {
                            HidePlayerManager.removeHidePlayer(player);
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (!onlinePlayer.equals(player)) {
                                    player.showPlayer(onlinePlayer);
                                }
                            }
                            player.getInventory().setItem(4, Items.playerHider.clone());
                            player.sendMessage(Messages.prefix + Messages.showPlayers);
                        }

                        HidePlayerManager.startToggleCooldown(player);
                        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
                    }
                }

                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if (event.getItem().getItemMeta().getDisplayName().equals(ItemTitles.LOBBY_SWITCHER)){
                        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2, 1);
                            player.openInventory(Inventories.LOBBY_SWITCHER);
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJumpAndRun(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ConfigManager manager = new ConfigManager();

        if (event.hasItem()){
            if (event.getItem().hasItemMeta()){
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if (event.getItem().getItemMeta().getDisplayName().equals(ItemTitles.EXIT_JUMP_AND_RUN)){
                        if (JumpAndRunUtils.isInJumpAndRun(player)){
                            JumpAndRunUtils.exitJumpAndRun(player);
                        }
                    }
                }

                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if (event.getItem().getItemMeta().getDisplayName().equals(ItemTitles.TELEPORT_TO_JUMP_AND_RUN_START)){
                        if (JumpAndRunUtils.isInJumpAndRun(player)){
                            player.teleport(manager.getLocation(Locations.JUMP_AND_RUN));
                        }
                    }
                }
            }
        }
    }
}
