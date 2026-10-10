package de.lobby.main.util;

import de.lobby.main.structure.Items;
import org.bukkit.entity.Player;

public class LobbyItems {


    public static void getLobbyItems(Player player){

        player.getInventory().setItem(0, Items.compass.clone());
        player.getInventory().setItem(4, HidePlayerManager.containsHideList(player)
                ? Items.playerShower.clone() : Items.playerHider.clone());
        player.getInventory().setItem(8, Items.lobbySwitcher.clone());
    }

    public static void removeLobbyItems(Player player){
        player.getInventory().clear();
    }
}
