package org.lunar.lunarGateway;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {

    private final LunarGateway plugin;

    public MenuClickListener(LunarGateway plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        // Encaminha o evento para o CompassManager, que gerencia os cliques no menu
        plugin.getCompassManager().handleMenuClick(event);
    }
}
