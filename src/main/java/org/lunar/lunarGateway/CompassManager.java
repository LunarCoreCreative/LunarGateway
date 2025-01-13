package org.lunar.lunarGateway;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CompassManager {

    private final LunarGateway plugin;
    private final FileConfiguration compassConfig;

    public CompassManager(LunarGateway plugin) {
        this.plugin = plugin;
        this.compassConfig = plugin.getCompassConfig();
    }

    /**
     * Cria e retorna a bússola configurada no compass.yml.
     *
     * @return ItemStack da bússola personalizada.
     */
    public ItemStack createCompass() {
        String icon = compassConfig.getString("icon", "COMPASS").toUpperCase();
        Material material = Material.matchMaterial(icon);
        if (material == null) {
            plugin.getLogger().warning("Material inválido configurado para a bússola: " + icon);
            material = Material.COMPASS; // Fallback para COMPASS
        }

        ItemStack compass = new ItemStack(material);
        ItemMeta meta = compass.getItemMeta();
        if (meta != null) {
            // Configura o nome
            String name = compassConfig.getString("name", "§aMenu Principal");
            meta.displayName(Component.text(name));

            // Configura a lore
            List<String> lore = compassConfig.getStringList("lore");
            if (lore != null && !lore.isEmpty()) {
                meta.lore(lore.stream().map(Component::text).toList());
            }
            compass.setItemMeta(meta);
        }
        return compass;
    }

    /**
     * Abre o menu configurado no compass.yml para o jogador.
     *
     * @param player Jogador para quem o menu será aberto.
     */
    public void openMenu(Player player) {
        String title = compassConfig.getString("menu.title", "§aMenu Principal");
        int size = compassConfig.getInt("menu.size", 27);

        // Garante que o tamanho do inventário seja múltiplo de 9
        if (size % 9 != 0) {
            plugin.getLogger().warning("Tamanho do inventário inválido, ajustando para 27.");
            size = 27;
        }

        Inventory menu = Bukkit.createInventory(null, size, Component.text(title));

        // Configura os itens do menu a partir do compass.yml
        List<?> items = compassConfig.getList("menu.items");
        if (items != null) {
            for (Object itemObj : items) {
                if (itemObj instanceof java.util.Map) {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> itemConfig = (java.util.Map<String, Object>) itemObj;

                    // Obtém os valores do item configurado
                    int slot = (int) itemConfig.getOrDefault("slot", -1);
                    String icon = (String) itemConfig.getOrDefault("icon", "STONE");
                    String itemName = (String) itemConfig.getOrDefault("name", "§aItem");
                    @SuppressWarnings("unchecked")
                    List<String> itemLore = (List<String>) itemConfig.getOrDefault("lore", List.of());

                    Material material = Material.matchMaterial(icon.toUpperCase());
                    if (material == null) {
                        plugin.getLogger().warning("Material inválido configurado para o item: " + icon);
                        continue;
                    }

                    ItemStack menuItem = new ItemStack(material);
                    ItemMeta meta = menuItem.getItemMeta();
                    if (meta != null) {
                        meta.displayName(Component.text(itemName));
                        meta.lore(itemLore.stream().map(Component::text).toList());
                        menuItem.setItemMeta(meta);
                    }

                    if (slot >= 0 && slot < size) {
                        menu.setItem(slot, menuItem);
                    } else {
                        plugin.getLogger().warning("Slot inválido configurado: " + slot);
                    }
                }
            }
        }

        // Preenche os slots vazios com itens de preenchimento, se configurado
        if (compassConfig.getBoolean("menu.filler.enabled", true)) {
            fillEmptySlots(menu);
        }

        player.openInventory(menu);
    }

    /**
     * Preenche os slots vazios do menu com um item configurado no filler.
     *
     * @param menu Inventário a ser preenchido.
     */
    private void fillEmptySlots(Inventory menu) {
        String fillerMaterial = compassConfig.getString("menu.filler.material", "BLACK_STAINED_GLASS_PANE");
        Material fillerType = Material.matchMaterial(fillerMaterial.toUpperCase());
        if (fillerType == null) {
            plugin.getLogger().warning("Material inválido para o preenchimento: " + fillerMaterial);
            fillerType = Material.BLACK_STAINED_GLASS_PANE; // Fallback
        }

        ItemStack fillerItem = new ItemStack(fillerType);
        ItemMeta fillerMeta = fillerItem.getItemMeta();
        if (fillerMeta != null) {
            String fillerName = compassConfig.getString("menu.filler.name", "§7");
            fillerMeta.displayName(Component.text(fillerName));

            List<String> fillerLore = compassConfig.getStringList("menu.filler.lore");
            if (!fillerLore.isEmpty()) {
                fillerMeta.lore(fillerLore.stream().map(Component::text).toList());
            }
            fillerItem.setItemMeta(fillerMeta);
        }

        for (int i = 0; i < menu.getSize(); i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, fillerItem);
            }
        }
    }

    /**
     * Verifica se o item clicado é um item do menu e executa o comando
     * correspondente.
     *
     * @param event Evento de clique no inventário.
     */
    public void handleMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String title = compassConfig.getString("menu.title", "§aMenu Principal");

        if (!event.getView().title().equals(Component.text(title)))
            return;

        event.setCancelled(true); // Impede que o jogador mova itens no menu

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;

        // Executa o comando associado ao item
        List<?> items = compassConfig.getList("menu.items");
        if (items != null) {
            for (Object itemObj : items) {
                if (itemObj instanceof java.util.Map) {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> itemConfig = (java.util.Map<String, Object>) itemObj;

                    String icon = (String) itemConfig.getOrDefault("icon", "STONE");
                    String command = (String) itemConfig.getOrDefault("command", "");

                    Material material = Material.matchMaterial(icon.toUpperCase());
                    if (material != null && clickedItem.getType() == material) {
                        if (!command.isEmpty()) {
                            player.closeInventory();
                            // Execute o comando como console
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                    command.replace("%player%", player.getName()));
                            plugin.getLogger().info("Executando comando como console: " + command);
                        } else {
                            plugin.getLogger().warning("Nenhum comando configurado para o item no slot.");
                        }
                        break;
                    }
                }
            }
        }
    }
}
