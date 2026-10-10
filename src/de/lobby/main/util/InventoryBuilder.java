package de.lobby.main.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class InventoryBuilder {

    public static Inventory createInventory(String title, int size){
        return Bukkit.createInventory(null, size, title);
    }
}
