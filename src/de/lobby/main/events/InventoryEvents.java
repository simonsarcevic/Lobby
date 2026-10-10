package de.lobby.main.events;

import de.lobby.main.structure.Inventories;
import de.lobby.main.structure.Items;
import de.lobby.main.util.InventoryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class InventoryEvents implements Listener {


    @EventHandler
    public void onInv(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if (event.hasItem()){
            if (event.getItem().hasItemMeta()){
                if (event.getItem().getItemMeta().getDisplayName().equals(Items.NAVIGATOR)){
                    if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
                        player.openInventory(Inventories.MENU_INVENTORY);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
