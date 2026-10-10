package de.lobby.main.util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HidePlayerManager {

    private static final long TOGGLE_COOLDOWN_MILLIS = 3_000L;

    private static final Set<UUID> hidePlayer = new HashSet<>();
    private static final Map<UUID, Long> toggleCooldowns = new HashMap<>();

    public static boolean containsHideList(Player player){
        return hidePlayer.contains(player.getUniqueId());
    }

    public static void setHidePlayer(Player player){
        hidePlayer.add(player.getUniqueId());
    }

    public static void removeHidePlayer(Player player){
        hidePlayer.remove(player.getUniqueId());
    }

    public static void startToggleCooldown(Player player) {
        toggleCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + TOGGLE_COOLDOWN_MILLIS);
    }

    public static long getRemainingCooldownSeconds(Player player) {
        Long cooldownUntil = toggleCooldowns.get(player.getUniqueId());
        if (cooldownUntil == null) {
            return 0;
        }

        long remainingMillis = cooldownUntil - System.currentTimeMillis();
        if (remainingMillis <= 0) {
            toggleCooldowns.remove(player.getUniqueId());
            return 0;
        }

        return (remainingMillis + 999L) / 1000L;
    }

    public static void clearPlayer(Player player) {
        hidePlayer.remove(player.getUniqueId());
        toggleCooldowns.remove(player.getUniqueId());
    }
}
