package de.lobby.main.util;

import de.lobby.main.structure.InventoryTitles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public final class DailyRewardInventory {

    private static final int[] REWARD_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private DailyRewardInventory() {
    }

    public static Inventory create(Player player, DailyRewardManager manager) {
        Inventory inventory = Bukkit.createInventory(null, 54, InventoryTitles.DAILY_REWARDS);
        ItemStack filler = createItem(
                new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " ", new String[0]
        );

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, filler.clone());
        }

        inventory.setItem(4, createPlayerHead(player, manager));

        int claimableDay = manager.getClaimableDay(player);

        for (int day = 1; day <= 28; day++) {
            inventory.setItem(REWARD_SLOTS[day - 1], createRewardItem(
                    day,
                    manager.getCoinsForDay(day),
                    manager.isClaimedDay(player, day),
                    day == claimableDay
            ));
        }

        inventory.setItem(49, createItem(
                new ItemStack(Material.BARRIER),
                "§c§lSchließen",
                new String[]{"  §7Klicke, um das Menü zu schließen."}
        ));
        return inventory;
    }

    public static int getRewardDay(int rawSlot) {
        for (int index = 0; index < REWARD_SLOTS.length; index++) {
            if (REWARD_SLOTS[index] == rawSlot) {
                return index + 1;
            }
        }
        return -1;
    }

    public static boolean isCloseSlot(int rawSlot) {
        return rawSlot == 49;
    }

    private static ItemStack createPlayerHead(Player player, DailyRewardManager manager) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName("§d§l" + player.getName());

        String status = manager.hasClaimedToday(player)
                ? "§cHeute bereits abgeholt"
                : "§aTag " + manager.getClaimableDay(player) + " ist bereit";
        meta.setLore(Arrays.asList(
                "  §7Aktuelle Streak: §e" + manager.getActiveStreak(player) + " Tage",
                "  " + status
        ));
        skull.setItemMeta(meta);
        return skull;
    }

    private static ItemStack createRewardItem(int day, long coins, boolean claimed,
                                              boolean claimable) {
        Material material;
        String status;

        if (claimed) {
            material = Material.MINECART;
            status = "§a✓ Bereits abgeholt";
        } else if (claimable) {
            material = Material.CHEST;
            status = "§e➜ Klicke zum Abholen";
        } else {
            material = Material.STORAGE_MINECART;
            status = "§c✗ Noch nicht verfügbar";
        }

        return createItem(
                new ItemStack(material),
                (claimable ? "§a§l" : "§c§l") + "Tag " + day,
                new String[]{
                        "  §7Belohnung:",
                        "  §6✦ " + coins + " Coins",
                        "  ",
                        "  " + status
                }
        );
    }

    private static ItemStack createItem(ItemStack itemStack, String displayName, String[] lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
