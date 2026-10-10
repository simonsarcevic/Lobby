package de.lobby.main.commands;

import de.lobby.main.structure.Messages;
import de.lobby.main.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLocationCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final String locationName;
    private final String displayName;
    private final String permission;

    public SetLocationCommand(ConfigManager configManager, String locationName,
                              String displayName, String permission) {
        this.configManager = configManager;
        this.locationName = locationName;
        this.displayName = displayName;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(permission) && !player.hasPermission("lobby.admin")) {
            player.sendMessage(Messages.prefix + Messages.noPermission);
            return true;
        }

        configManager.setLocation(locationName, player.getLocation());
        player.sendMessage(Messages.prefix + "§7Die Position für §a" + displayName + " §7wurde gesetzt!");
        return true;
    }
}
