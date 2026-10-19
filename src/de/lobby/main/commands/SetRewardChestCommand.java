package de.lobby.main.commands;

import de.lobby.main.structure.Messages;
import de.lobby.main.util.ConfigManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class SetRewardChestCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final String locationName;
    private final String permission;

    public SetRewardChestCommand(ConfigManager configManager, String locationName, String permission){
        this.configManager = configManager;
        this.locationName = locationName;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(Messages.onlyPlayers);
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(permission) && !player.hasPermission("lobby.admin")) {
            player.sendMessage(Messages.prefix + Messages.noPermission);
            return true;
        }

        Block targetBlock = player.getTargetBlock((Set<Material>) null, 6);
        if (targetBlock == null || (targetBlock.getType() != Material.CHEST
                && targetBlock.getType() != Material.TRAPPED_CHEST)) {
            player.sendMessage(Messages.prefix + Messages.lookAtChest);
            return true;
        }

        configManager.setLocation(locationName, targetBlock.getLocation());
        player.sendMessage(Messages.prefix + Messages.rewardChestSet);
        return true;
    }
}
