package de.lobby.main.util;

import de.lobby.main.structure.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LobbyItems {


    public static void getLobbyItems(Player player){

        player.getInventory().setItem(0, Items.compass);
        player.getInventory().setItem(4, Items.playerHider);
        player.getInventory().setItem(8, Items.lobbySwitcher);
    }

    public static void removeLobbyItems(Player player){
        player.getInventory().clear();
    }
}
