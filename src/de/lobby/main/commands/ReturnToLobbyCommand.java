package de.lobby.main.commands;

import de.lobby.main.structure.Messages;
import de.lobby.main.util.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReturnToLobbyCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public ReturnToLobbyCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        Location spawn = configManager.getSpawn();
        if (spawn == null) {
            player.sendMessage(Messages.prefix + Messages.locationNotSet);
            return true;
        }

        player.teleport(spawn);
        player.playSound(spawn, Sound.ENDERMAN_TELEPORT, 1, 1);
        return true;
    }
}
