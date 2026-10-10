package de.lobby.main.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BuildModeUtils {

    private static final List<Player> buildMode = new ArrayList<>();

    public boolean isPlayerInBuildMode(Player player){
        return buildMode.contains(player);
    }

    public void setBuildMode(Player player){
        buildMode.add(player);
    }

    public void removePlayerFromBuildMode(Player player){
        buildMode.remove(player);
    }

    public void clearBuildMode(){
        buildMode.clear();
    }
}
