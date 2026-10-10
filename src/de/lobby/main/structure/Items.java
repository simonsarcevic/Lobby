package de.lobby.main.structure;

import de.lobby.main.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Items {

    ItemStack compass = ItemBuilder.createItem(new ItemStack(Material.COMPASS), ItemTitles.NAVIGATOR, 1);
    ItemStack playerHider = ItemBuilder.createItem(new ItemStack(Material.REDSTONE), ItemTitles.HIDE_PLAYER, 1);
    ItemStack lobbySwitcher = ItemBuilder.createItem(new ItemStack(Material.NETHER_STAR), ItemTitles.LOBBY_SWITCHER, 1);
}
