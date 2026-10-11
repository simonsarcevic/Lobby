package de.lobby.main.util;

import de.lobby.main.structure.Items;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class JumpAndRunUtils {

    private static final Set<UUID> activePlayers = new HashSet<>();

    public static void getJumpAndRunItems(Player player){
        LobbyItems.removeLobbyItems(player);
        player.getInventory().setItem(1, Items.EXIT_JUMP_AND_RUN);
        player.getInventory().setItem(3, Items.BACK_TO_START_JUMP_AND_RUN);
    }

    public static void removeJumpAndRunItems(Player player){
        player.getInventory().clear();
        LobbyItems.getLobbyItems(player);
    }

    public static boolean isInJumpAndRun(Player player){
        return activePlayers.contains(player.getUniqueId());
    }

    public static void startJumpAndRun(Player player){
        getJumpAndRunItems(player);
        activePlayers.add(player.getUniqueId());
    }

    public static void exitJumpAndRun(Player player){
        removeJumpAndRunItems(player);
        activePlayers.remove(player.getUniqueId());
    }

    public static void clearPlayer(Player player){
        activePlayers.remove(player.getUniqueId());
    }
}
