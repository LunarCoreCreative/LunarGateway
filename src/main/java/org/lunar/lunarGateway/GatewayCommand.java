package org.lunar.lunarGateway;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GatewayCommand implements CommandExecutor {

    private final LunarGateway plugin;

    public GatewayCommand(LunarGateway plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando!");
            return true;
        }

        Player player = (Player) sender;

        // Exibe mensagem temporária
        player.sendMessage("§aMenu principal do LunarGateway ainda em construção!");
        return true;
    }
}
