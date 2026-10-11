package de.lobby.main.commands;

import de.lobby.main.structure.Messages;
import de.lobby.main.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetJumpAndRunStartCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final String locationName;
    private final String permission;

    public SetJumpAndRunStartCommand(ConfigManager configManager, String locationName, String permission){
        this.configManager = configManager;
        this.locationName = locationName;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(permission)){
            player.sendMessage(Messages.prefix + Messages.noPermission);
            return true;
        }

        configManager.setLocation(locationName, player.getLocation());
        player.sendMessage(Messages.prefix + Messages.setJumpAndRunLocation);
        return false;
    }
}
