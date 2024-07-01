package com.uaulight.leventos;

import com.google.common.io.Resources;
import com.uaulight.leventos.Eventos.Bolao;
import com.uaulight.leventos.Eventos.Loteria;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class Main extends JavaPlugin {
    public static Main plugin;

    public static Economy economy = null;

    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        try {
            File file = new File(getDataFolder() + File.separator, "config.yml");
            String allText = Resources.toString(file.toURI().toURL(), StandardCharsets.UTF_8);
            getConfig().loadFromString(allText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupEconomy();
        getCommand("evento").setExecutor(new Comandos());
        if (getConfig().getBoolean("Eventos.Loteria"))
            getCommand("loteria").setExecutor(new Loteria());
        if (getConfig().getBoolean("Eventos.Bolao"))
            getCommand("bolao").setExecutor(new Bolao());
        getLogger().info("Plugin ativado com sucesso!");
        getLogger().info("Versao: " + getDescription().getVersion());
        getLogger().info("Autor: " + getDescription().getAuthors());
    }

    public void onDisable() {
        getLogger().info("Plugin desativado com sucesso!");
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null)
            economy = economyProvider.getProvider();
    }
}