package de.lobby.main.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.lobby.main.Main;
import org.bukkit.entity.Player;

public final class ServerConnector {

    private ServerConnector() {
    }

    public static void connect(Player player, String serverName) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(serverName);

        player.sendPluginMessage(Main.getInstance(), "BungeeCord", output.toByteArray());
    }
}
