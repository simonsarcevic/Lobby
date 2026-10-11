package de.lobby.main.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.lobby.main.Main;
import org.bukkit.entity.Player;

public final class ServerConnector {

    public static final String NAVIGATION_CHANNEL = "LobbyNavigate";

    private ServerConnector() {
    }

    public static void connect(Player player, String serverName) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(serverName);

        player.sendPluginMessage(Main.getInstance(), "BungeeCord", output.toByteArray());
    }

    public static void navigate(Player player, String locationName) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Navigate");
        output.writeUTF(locationName);

        player.sendPluginMessage(Main.getInstance(), NAVIGATION_CHANNEL, output.toByteArray());
    }

    public static void requestPendingDestination(Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("RequestDestination");

        player.sendPluginMessage(Main.getInstance(), NAVIGATION_CHANNEL, output.toByteArray());
    }
}
