package com.uaulight.leventos.Eventos;

import java.util.ArrayList;
import java.util.Random;

import com.uaulight.leventos.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RandomVIP {
    private static boolean acontecendo = false;

    public static void iniciar() {
        if (acontecendo)
            return;
        acontecendo = true;
        if (Bukkit.getOnlinePlayers().size() > Main.plugin.getConfig().getInt("RandomVIP.Minimo_Online")) {
            final ArrayList<Player> online = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                online.add(p);
                p.sendMessage(" ");
                p.sendMessage("§6§l[Sorteio] §6Sorteando VIP...");
                p.sendMessage(" ");
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)Main.plugin, new Runnable() {
                public void run() {
                    Player ganhador = Bukkit.getPlayer(((Player)online.get((new Random()).nextInt(online.size()))).getName());
                    Bukkit.broadcastMessage(" ");
                    Bukkit.broadcastMessage("§e§l[Sorteio] §6Sorteio finalizado.");
                    Bukkit.broadcastMessage("§e§l[Sorteio] §6O vencedor do sorteio foi §b" + ganhador.getName() + "§e.");
                    Bukkit.broadcastMessage(" ");
                    Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), Main.plugin.getConfig().getString("RandomVIP.Comando").replace("@jogador", ganhador.getName()));
                    online.clear();
                    RandomVIP.acontecendo = false;
                }
            },200L);
        } else {
            Main.plugin.getLogger().warning("Numero de jogadores insuficiente para iniciar o evento 'RandomVIP'.");
        }
    }
}