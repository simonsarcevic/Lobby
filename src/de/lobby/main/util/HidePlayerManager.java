package de.lobby.main.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HidePlayerManager {


    private static final List<Player> hidePlayer = new ArrayList<>();

    public boolean containsHideList(Player player){
        return hidePlayer.contains(player);
    }

    public static void setHidePlayer(Player player){
        hidePlayer.add(player);
    }

    public static void removeHidePlayer(Player player){
        hidePlayer.remove(player);
    }
}
