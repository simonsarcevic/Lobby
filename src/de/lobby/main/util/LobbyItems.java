package de.lobby.main.util;

import de.lobby.main.structure.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LobbyItems {


    public static void getLobbyItems(Player player){
        ItemStack compass = ItemBuilder.createItem(new ItemStack(Material.COMPASS), Items.NAVIGATOR, 1);
        ItemStack playerHider = ItemBuilder.createItem(new ItemStack(Material.REDSTONE), Items.HIDE_PLAYER, 1);
        ItemStack lobbySwitcher = ItemBuilder.createItem(new ItemStack(Material.NETHER_STAR), Items.LOBBY_SWITCHER, 1);

        player.getInventory().setItem(0, compass);
        player.getInventory().setItem(4, playerHider);
        player.getInventory().setItem(8, lobbySwitcher);
    }

    public static void removeLobbyItems(Player player){
        player.getInventory().clear();
    }
}
