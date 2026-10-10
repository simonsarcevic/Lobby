package de.lobby.main.commands;

import de.lobby.main.structure.Messages;
import de.lobby.main.util.ConfigManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public LobbyCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("lobby.set") && !player.hasPermission("lobby.admin")) {
            player.sendMessage(Messages.prefix + Messages.noPermission);
            return true;
        }

        Location loc = player.getLocation();
        configManager.setSpawn(loc);
        player.sendMessage(Messages.prefix + Messages.spawnSet);
        return true;
    }
}
