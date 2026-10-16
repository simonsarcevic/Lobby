package de.lobby.main.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DailyRewardManager {

    private static final int COINS_PER_DAY = 100;

    private final File file;
    private final FileConfiguration configuration;
    private final Map<UUID, PendingClaim> pendingClaims = new HashMap<>();

    public DailyRewardManager(JavaPlugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new IllegalStateException("Der Plugin-Ordner konnte nicht erstellt werden: " + dataFolder);
        }

        file = new File(dataFolder, "daily-rewards.yml");
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public int getActiveStreak(Player player) {
        LocalDate lastClaim = getLastClaim(player);
        if (lastClaim == null) {
            return 0;
        }

        LocalDate today = LocalDate.now();
        if (!lastClaim.equals(today) && !lastClaim.equals(today.minusDays(1))) {
            return 0;
        }

        return Math.max(0, configuration.getInt(getPlayerPath(player) + ".streak"));
    }

    public boolean hasClaimedToday(Player player) {
        LocalDate lastClaim = getLastClaim(player);
        return lastClaim != null && lastClaim.equals(LocalDate.now());
    }

    public int getClaimableDay(Player player) {
        if (hasClaimedToday(player)) {
            return -1;
        }
        return getActiveStreak(player) % 28 + 1;
    }

    public long getCoinsForDay(int day) {
        return (long) day * COINS_PER_DAY;
    }

    public boolean claim(Player player, int day) {
        if (!reserveClaim(player, day)) {
            return false;
        }

        return completeClaim(player);
    }

    public boolean reserveClaim(Player player, int day) {
        UUID playerId = player.getUniqueId();
        if (pendingClaims.containsKey(playerId)) {
            return false;
        }

        int claimableDay = getClaimableDay(player);
        if (claimableDay == -1 || day != claimableDay) {
            return false;
        }

        pendingClaims.put(playerId, new PendingClaim(
                player.getName(),
                getActiveStreak(player) + 1,
                LocalDate.now()
        ));
        return true;
    }

    public boolean completeClaim(Player player) {
        PendingClaim pendingClaim = pendingClaims.get(player.getUniqueId());
        if (pendingClaim == null) {
            return false;
        }

        String path = getPlayerPath(player);
        configuration.set(path + ".name", pendingClaim.playerName);
        configuration.set(path + ".streak", pendingClaim.streak);
        configuration.set(path + ".last-claim", pendingClaim.claimDate.toString());
        save();
        pendingClaims.remove(player.getUniqueId());
        return true;
    }

    public void cancelClaim(Player player) {
        pendingClaims.remove(player.getUniqueId());
    }

    public boolean hasPendingClaim(Player player) {
        return pendingClaims.containsKey(player.getUniqueId());
    }

    public boolean isClaimedDay(Player player, int day) {
        if (day < 1 || day > 28) {
            return false;
        }

        int streak = getActiveStreak(player);
        if (streak == 0) {
            return false;
        }

        int claimedThrough;
        if (hasClaimedToday(player)) {
            claimedThrough = (streak - 1) % 28 + 1;
        } else {
            claimedThrough = streak % 28;
        }
        return day <= claimedThrough;
    }

    private LocalDate getLastClaim(Player player) {
        String savedDate = configuration.getString(getPlayerPath(player) + ".last-claim");
        if (savedDate == null || savedDate.isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(savedDate);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    private String getPlayerPath(Player player) {
        return "players." + player.getUniqueId().toString();
    }

    private void save() {
        try {
            configuration.save(file);
        } catch (IOException exception) {
            throw new IllegalStateException("daily-rewards.yml konnte nicht gespeichert werden.", exception);
        }
    }

    private static final class PendingClaim {
        private final String playerName;
        private final int streak;
        private final LocalDate claimDate;

        private PendingClaim(String playerName, int streak, LocalDate claimDate) {
            this.playerName = playerName;
            this.streak = streak;
            this.claimDate = claimDate;
        }
    }
}
