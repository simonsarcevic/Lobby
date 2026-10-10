package de.lobby.main.commands;

import de.lobby.main.structure.Messages;
import de.lobby.main.util.BuildModeUtils;
import de.lobby.main.util.LobbyItems;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;
        BuildModeUtils buildModeUtils = new BuildModeUtils();

        if (!player.hasPermission("lobby.build") && !player.hasPermission("lobby.admin")){
            player.sendMessage(Messages.prefix + Messages.noPermission);
            return true;
        }

        if (!buildModeUtils.isPlayerInBuildMode(player)){
            buildModeUtils.setBuildMode(player);
            player.setGameMode(GameMode.CREATIVE);
            LobbyItems.removeLobbyItems(player);
            player.sendMessage(Messages.prefix + Messages.activateBuild);
        }else{
            buildModeUtils.removePlayerFromBuildMode(player);
            player.setGameMode(GameMode.ADVENTURE);
            LobbyItems.getLobbyItems(player);
            player.sendMessage(Messages.prefix + Messages.deactivateBuild);
        }
        return false;
    }
}
