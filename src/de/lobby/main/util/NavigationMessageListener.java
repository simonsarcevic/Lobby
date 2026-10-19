package de.lobby.main.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.lobby.main.Main;
import de.lobby.main.structure.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NavigationMessageListener implements PluginMessageListener {

    private static final Set<String> ALLOWED_DESTINATIONS = new HashSet<>(Arrays.asList(
            "spawn", "training", "kitpvp", "dailychests"
    ));

    private final ConfigManager configManager;

    public NavigationMessageListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player carrier, byte[] message) {
        if (!ServerConnector.NAVIGATION_CHANNEL.equals(channel)) {
            return;
        }

        try {
            ByteArrayDataInput input = ByteStreams.newDataInput(message);
            if (!"Destination".equals(input.readUTF())) {
                return;
            }

            UUID playerId = UUID.fromString(input.readUTF());
            String destination = input.readUTF().toLowerCase();
            if (!ALLOWED_DESTINATIONS.contains(destination)) {
                return;
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> teleport(playerId, destination));
        } catch (RuntimeException exception) {
            Main.getInstance().getLogger().warning(
                    "Ungültige Navigationsnachricht verworfen: " + exception.getMessage()
            );
        }
    }

    private void teleport(UUID playerId, String destination) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null || !player.isOnline()) {
            return;
        }

        Location location = configManager.getLocation(destination);
        if (location == null) {
            player.sendMessage(Messages.prefix + Messages.locationNotSet);
            return;
        }

        player.teleport(location);
        player.playSound(location, Sound.ENDERMAN_TELEPORT, 1, 1);
    }
}
