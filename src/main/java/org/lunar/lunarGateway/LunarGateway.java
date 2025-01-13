package org.lunar.lunarGateway;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class LunarGateway extends JavaPlugin {

    private FileConfiguration compassConfig; // Configuração específica da bússola
    private CompassManager compassManager; // Gerenciador de bússola

    @Override
    public void onEnable() {
        // Mensagem de ativação
        getLogger().info("LunarGateway ativado com sucesso!");

        // Salva as configurações padrão
        saveDefaultConfig();
        loadCompassConfig();

        // Inicializa o CompassManager
        compassManager = new CompassManager(this);

        // Registra eventos e comandos
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Mensagem de desativação
        getLogger().info("LunarGateway desativado!");
    }

    /**
     * Carrega ou cria o arquivo compass.yml.
     */
    private void loadCompassConfig() {
        File compassFile = new File(getDataFolder(), "compass.yml");

        // Cria o arquivo compass.yml se ele não existir
        if (!compassFile.exists()) {
            saveResource("compass.yml", false);
        }

        compassConfig = YamlConfiguration.loadConfiguration(compassFile);
    }

    /**
     * Obtém a configuração da bússola (compass.yml).
     *
     * @return Configuração da bússola.
     */
    public FileConfiguration getCompassConfig() {
        return compassConfig;
    }

    /**
     * Salva alterações no arquivo compass.yml.
     */
    public void saveCompassConfig() {
        try {
            compassConfig.save(new File(getDataFolder(), "compass.yml"));
        } catch (IOException e) {
            getLogger().severe("Não foi possível salvar o arquivo compass.yml!");
            e.printStackTrace();
        }
    }

    /**
     * Registra os eventos necessários para o plugin.
     */
    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MenuClickListener(this), this); // Listener para o menu
    }

    /**
     * Registra os comandos do plugin.
     */
    private void registerCommands() {
        if (getCommand("lunargateway") != null) {
            getCommand("lunargateway").setExecutor(new GatewayCommand(this));
            getLogger().info("Comando /lunargateway registrado com sucesso.");
        } else {
            getLogger().severe("Erro ao registrar o comando /lunargateway.");
        }

        if (getCommand("lobby") != null) {
            getCommand("lobby").setExecutor(new LobbyCommand(this));
            getLogger().info("Comando /lobby registrado com sucesso.");
        } else {
            getLogger().severe("Erro ao registrar o comando /lobby.");
        }

        if (getCommand("menu") != null) {
            getCommand("menu").setExecutor(new MenuCommand(this));
            getLogger().info("Comando /menu registrado com sucesso.");
        } else {
            getLogger().severe("Erro ao registrar o comando /menu.");
        }
    }

    /**
     * Obtém o gerenciador da bússola.
     *
     * @return CompassManager
     */
    public CompassManager getCompassManager() {
        return compassManager;
    }
}
