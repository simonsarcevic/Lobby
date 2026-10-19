package de.lobby.main.events;

import de.lobby.main.structure.*;
import de.lobby.main.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryEvents implements Listener {

    private final JavaPlugin plugin;
    private final DailyRewardManager dailyRewardManager;
    private final CoinSystemBridge coinSystemBridge;

    public InventoryEvents(JavaPlugin plugin, DailyRewardManager dailyRewardManager,
                           CoinSystemBridge coinSystemBridge) {
        this.plugin = plugin;
        this.dailyRewardManager = dailyRewardManager;
        this.coinSystemBridge = coinSystemBridge;
    }

    @EventHandler
    public void onInv(InventoryClickEvent event){
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        BuildModeUtils buildModeUtils = new BuildModeUtils();
        boolean lobbySwitcher = event.getInventory().getTitle().equals(InventoryTitles.LOBBY_SWITCHER);
        boolean navigator = event.getInventory().getTitle().equals(InventoryTitles.NAVIGATOR);
        boolean dailyRewards = event.getInventory().getTitle().equals(InventoryTitles.DAILY_REWARDS);

        if (!buildModeUtils.isPlayerInBuildMode(player) || lobbySwitcher || navigator || dailyRewards) {
            event.setCancelled(true);
        }

        if (dailyRewards) {
            handleDailyRewardClick(event, player);
            return;
        }

        if (!lobbySwitcher && !navigator) return;

        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;

        String itemTitle = event.getCurrentItem().getItemMeta().getDisplayName();

        if (navigator) {
            if (itemTitle.equals(ItemTitles.TRAINING)) {
                connectToServer(player, "training");
            } else if (itemTitle.equals(ItemTitles.KITPVP)) {
                connectToServer(player, "kitpvp");
            } else if (itemTitle.equals(ItemTitles.SPAWN)) {
                navigateTo(player, "spawn");
            } else if (itemTitle.equals(ItemTitles.DAILY_REWARDS)) {
                navigateTo(player, "dailychests");
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
                || event.getInventory().getTitle().equals(InventoryTitles.NAVIGATOR)
                || event.getInventory().getTitle().equals(InventoryTitles.DAILY_REWARDS)) {
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

    private void navigateTo(Player player, String locationName) {
        player.closeInventory();
        ServerConnector.navigate(player, locationName);
    }

    private void connectToServer(Player player, String serverName) {
        player.closeInventory();
        ServerConnector.connect(player, serverName);
    }

    private void handleDailyRewardClick(InventoryClickEvent event, Player player) {
        if (DailyRewardInventory.isCloseSlot(event.getRawSlot())) {
            player.closeInventory();
            return;
        }

        int clickedDay = DailyRewardInventory.getRewardDay(event.getRawSlot());
        if (clickedDay == -1) {
            return;
        }

        int claimableDay = dailyRewardManager.getClaimableDay(player);
        if (claimableDay == -1) {
            player.sendMessage(Messages.prefix + Messages.dailyRewardAlreadyClaimed);
            return;
        }

        if (clickedDay != claimableDay) {
            player.sendMessage(Messages.prefix + Messages.dailyRewardWrongDay.replace(
                    "%day%", String.valueOf(claimableDay)
            ));
            return;
        }

        if (!dailyRewardManager.reserveClaim(player, clickedDay)) {
            if (dailyRewardManager.hasPendingClaim(player)) {
                player.sendMessage(Messages.prefix + Messages.dailyRewardProcessing);
            }
            return;
        }

        long coins = dailyRewardManager.getCoinsForDay(clickedDay);
        player.closeInventory();

        if (coinSystemBridge == null) {
            dailyRewardManager.cancelClaim(player);
            player.sendMessage(Messages.prefix + Messages.dailyRewardPayoutFailed);
            return;
        }

        coinSystemBridge.addCoins(player.getUniqueId(), player.getName(), coins)
                .whenComplete((newBalance, error) -> Bukkit.getScheduler().runTask(plugin, () -> {
                    if (error != null) {
                        dailyRewardManager.cancelClaim(player);
                        plugin.getLogger().warning("Daily-Reward-Auszahlung für "
                                + player.getName() + " fehlgeschlagen: " + error.getMessage());
                        if (player.isOnline()) {
                            player.sendMessage(Messages.prefix + Messages.dailyRewardPayoutFailed);
                        }
                        return;
                    }

                    if (!dailyRewardManager.completeClaim(player)) {
                        plugin.getLogger().severe("Daily Reward für " + player.getName()
                                + " wurde ausgezahlt, konnte aber nicht gespeichert werden.");
                        return;
                    }

                    if (player.isOnline()) {
                        player.sendMessage(Messages.prefix + Messages.dailyRewardClaimed
                                .replace("%day%", String.valueOf(clickedDay))
                                .replace("%coins%", String.valueOf(coins)));
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 2, 1);
                    }
                }));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if (event.hasItem()){
            if (event.getItem().hasItemMeta()
                    && event.getItem().getItemMeta().hasDisplayName()){
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
            if (event.getItem().hasItemMeta()
                    && event.getItem().getItemMeta().hasDisplayName()){
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
