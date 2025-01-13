package org.lunar.lunarGateway;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlayerJoinListener implements Listener {

    private final LunarGateway plugin;

    public PlayerJoinListener(LunarGateway plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Envia mensagem de boas-vindas
        String welcomeMessage = plugin.getConfig().getString("messages.welcome", "§aBem-vindo ao servidor!");
        player.sendMessage(welcomeMessage);

        // Verifica se o teleporte ao mundo inicial está ativado
        boolean onJoinWorldEnabled = plugin.getConfig().getBoolean("on-join-world.enabled", true);
        if (onJoinWorldEnabled) {
            String defaultWorld = plugin.getConfig().getString("on-join-world.default-world", "world");
            if (Bukkit.getWorld(defaultWorld) != null) {
                player.teleport(Bukkit.getWorld(defaultWorld).getSpawnLocation());
            } else {
                String noWorldMessage = plugin.getConfig().getString("messages.no-world",
                        "§cO mundo configurado não existe!");
                player.sendMessage(noWorldMessage);
                plugin.getLogger().warning("O mundo definido como 'default-world' não existe: " + defaultWorld);
            }
        }

        // Verifica se a funcionalidade de bússola está habilitada
        boolean compassEnabled = plugin.getCompassConfig().getBoolean("enabled", true);
        String compassWorld = plugin.getCompassConfig().getString("world", "world");

        // Dá a bússola ao jogador se estiver no mundo configurado
        if (compassEnabled && player.getWorld().getName().equalsIgnoreCase(compassWorld)) {
            giveCompass(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        boolean compassLocked = plugin.getCompassConfig().getBoolean("locked", true);
        if (!compassLocked)
            return;

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null && currentItem.getType() == getCompassMaterial() && isLunarCompass(currentItem)) {
            event.setCancelled(true); // Impede mover a bússola
            player.sendMessage("§cVocê não pode mover a bússola mágica!");
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        boolean compassLocked = plugin.getCompassConfig().getBoolean("locked", true);
        if (!compassLocked)
            return;

        ItemStack droppedItem = event.getItemDrop().getItemStack();
        if (droppedItem.getType() == getCompassMaterial() && isLunarCompass(droppedItem)) {
            event.setCancelled(true); // Impede dropar a bússola
            player.sendMessage("§cVocê não pode dropar a bússola mágica!");
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        // Verifica se o item na mão é a bússola mágica
        if (itemInHand != null && isLunarCompass(itemInHand)) {
            event.setCancelled(true); // Cancela a interação padrão
            plugin.getCompassManager().openMenu(player); // Abre o menu configurado
        }
    }

    private void giveCompass(Player player) {
        Material compassMaterial = getCompassMaterial();
        if (compassMaterial == null)
            return;

        ItemStack compass = new ItemStack(compassMaterial);
        ItemMeta meta = compass.getItemMeta();
        if (meta != null) {
            // Configura o nome personalizado
            String name = plugin.getCompassConfig().getString("name", "§aMenu Principal");
            meta.displayName(Component.text(name));

            // Configura a lore personalizada
            List<String> lore = plugin.getCompassConfig().getStringList("lore");
            if (lore != null && !lore.isEmpty()) {
                meta.lore(lore.stream().map(Component::text).toList());
            }

            compass.setItemMeta(meta);
        }

        // Define o slot configurado
        int slot = plugin.getCompassConfig().getInt("slot", 0);
        if (slot >= 0 && slot < player.getInventory().getSize()) {
            player.getInventory().setItem(slot, compass);
        } else {
            player.getInventory().addItem(compass);
        }

        String compassMessage = plugin.getConfig().getString("messages.compass-received",
                "§eVocê recebeu a bússola mágica!");
        player.sendMessage(compassMessage);
    }

    private Material getCompassMaterial() {
        String icon = plugin.getCompassConfig().getString("icon", "COMPASS").toUpperCase();
        Material material = Material.matchMaterial(icon);
        if (material == null) {
            plugin.getLogger().warning("Material inválido configurado para a bússola: " + icon);
        }
        return material;
    }

    private boolean isLunarCompass(ItemStack item) {
        if (item == null || item.getType() != getCompassMaterial())
            return false;
        ItemMeta meta = item.getItemMeta();
        String expectedName = plugin.getCompassConfig().getString("name", "§aMenu Principal");
        return meta != null && Component.text(expectedName).equals(meta.displayName());
    }
}
