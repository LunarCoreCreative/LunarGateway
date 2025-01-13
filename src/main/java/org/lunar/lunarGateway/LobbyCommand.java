package org.lunar.lunarGateway;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {

    private final LunarGateway plugin;

    public LobbyCommand(LunarGateway plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verifica se o comando está habilitado
        if (!plugin.getConfig().getBoolean("lobby.enabled", true)) {
            sender.sendMessage("§cO comando /lobby está desativado!");
            return true;
        }

        // Verifica se quem executa é um jogador
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando!");
            return true;
        }

        Player player = (Player) sender;

        // Obtém o nome do mundo do config.yml
        String worldName = plugin.getConfig().getString("lobby.world", "world");
        World lobbyWorld = Bukkit.getWorld(worldName);

        if (lobbyWorld == null) {
            // Mensagem se o mundo configurado não existir
            String noWorldMessage = plugin.getConfig().getString("lobby.no-world-message",
                    "§cO mundo configurado para o lobby não existe!");
            player.sendMessage(noWorldMessage);
            plugin.getLogger().warning("O mundo configurado para o lobby não foi encontrado: " + worldName);
            return true;
        }

        // Teleporta o jogador para o spawn do mundo configurado
        player.teleport(lobbyWorld.getSpawnLocation());
        String lobbyMessage = plugin.getConfig().getString("lobby.message", "§aVocê foi teleportado para o lobby!");
        player.sendMessage(lobbyMessage);

        return true;
    }
}
