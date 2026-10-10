package de.lobby.main.structure;

import de.lobby.main.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Items {

    String LOBBY_SKULL_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFkZDRmZTRhNDI5YWJkNjY1ZGZkYjNlMjEzMjFkNmVmYTZhNmI1ZTdiOTU2ZGI5YzVkNTljOWVmYWIyNSJ9fX0=";
    String DAILY_REWARDS_SKULL_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MxYjJmNTkyY2ZjOGQzNzJkY2Y1ZmQ0NGVlZDY5ZGRkYzY0NjAxZDc4NDZkNzI2MTlmNzA1MTFkODA0M2E4OSJ9fX0=";
    String SPAWN_SKULL_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTdlYWI5ZTVjNzZlMTM2MWIxOTgyNjAyYTYxYzYxNjE4ZDNmNTg5YjI3YzhiM2U0NDcyNDAzNjE1NTliOTU4MSJ9fX0=";
    String KITPVP_SKULL_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDMzMzkxYWM1MWE3ZTk1MjUxMjdiYzM4OTdjMmYzYmI1Yzc5M2ZkMjkzN2IyMDEyMjI5NzUzNzAxMTdiMzViMCJ9fX0=";
    String TRAINING_SKULL_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWUwNGE5NjFiNjYxMDFkNGVmNWZmOGUyYWEyZGE0ODUwYWY4MTBjOWUwYzg4NDE2NmVkYTA0ZmRiNDExMWEwMiJ9fX0=";

    ItemStack compass = ItemBuilder.createItem(new ItemStack(Material.COMPASS), ItemTitles.NAVIGATOR, 1);
    ItemStack playerHider = ItemBuilder.createItem(new ItemStack(Material.REDSTONE), ItemTitles.HIDE_PLAYER, 1);
    ItemStack playerShower = ItemBuilder.createItem(new ItemStack(Material.SULPHUR), ItemTitles.SHOW_PLAYER, 1);
    ItemStack lobbySwitcher = ItemBuilder.createItem(new ItemStack(Material.NETHER_STAR), ItemTitles.LOBBY_SWITCHER, 1);
    ItemStack lobbySwitcherFiller = ItemBuilder.createItem(
            new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " ", 1
    );
    ItemStack lobby = ItemBuilder.createCustomSkull(ItemTitles.LOBBY, LOBBY_SKULL_TEXTURE);
    ItemStack lobby1 = ItemBuilder.createCustomSkull(ItemTitles.LOBBY_2, LOBBY_SKULL_TEXTURE);
    ItemStack lobby2 = ItemBuilder.createCustomSkull(ItemTitles.LOBBY_3, LOBBY_SKULL_TEXTURE);

    ItemStack navigatorFiller = ItemBuilder.createItem(
            new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " ", 1
    );
    ItemStack training = ItemBuilder.createCustomSkull(
            ItemTitles.TRAINING, TRAINING_SKULL_TEXTURE,
            "§7Klicke, um zum Training zu gelangen."
    );
    ItemStack kitpvp = ItemBuilder.createCustomSkull(
            ItemTitles.KITPVP, KITPVP_SKULL_TEXTURE,
            "§7Klicke, um zu KitPvP zu gelangen."
    );
    ItemStack spawn = ItemBuilder.createCustomSkull(
            ItemTitles.SPAWN, SPAWN_SKULL_TEXTURE,
            "§7Klicke, um zum Spawn zu gelangen."
    );
    ItemStack dailyRewards = ItemBuilder.createCustomSkull(
            ItemTitles.DAILY_REWARDS, DAILY_REWARDS_SKULL_TEXTURE,
            "§7Klicke, um zu den Daily Rewards zu gelangen."
    );
}
