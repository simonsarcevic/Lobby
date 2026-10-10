package de.lobby.main.structure;

import de.lobby.main.util.InventoryBuilder;
import org.bukkit.inventory.Inventory;

public interface Inventories {

    Inventory MENU_INVENTORY = InventoryBuilder.createInventory(InventoryTitles.NAVIGATOR, 27);
}
