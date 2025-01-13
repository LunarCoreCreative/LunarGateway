package org.lunar.lunarGateway;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {

    private final LunarGateway plugin;

    public MenuCommand(LunarGateway plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verifica se o comando está habilitado no compass.yml
        boolean menuEnabled = plugin.getCompassConfig().getBoolean("menu.enabled", true);
        if (!menuEnabled) {
            sender.sendMessage("§cO comando /menu está desativado!");
            return true;
        }

        // Verifica se quem executa é um jogador
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando!");
            return true;
        }

        Player player = (Player) sender;

        // Abre o menu configurado para o jogador
        plugin.getCompassManager().openMenu(player);
        return true;
    }
}
