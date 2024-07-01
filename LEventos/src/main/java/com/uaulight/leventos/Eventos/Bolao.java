package com.uaulight.leventos.Eventos;

import java.util.ArrayList;
import java.util.Random;
import com.uaulight.leventos.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Bolao implements CommandExecutor {
    private static boolean acontecendo = false;

    private static int aposta_total = 0;

    private static final ArrayList<Player> participantes = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("bolao")) {
            if (!acontecendo) {
                p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Nao_Acontecendo").replace("&", "§"));
                return true;
            }
            if (participantes.contains(p)) {
                p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Bolao_Erro_Participando").replace("&", "§"));
                return true;
            }
            if (Main.economy.getBalance(p.getName()) < Main.plugin.getConfig().getInt("Bolao.Valor_Aposta")) {
                p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Bolao_Erro_Saldo").replace("&", "§"));
                return true;
            }
            aposta_total += Main.plugin.getConfig().getInt("Bolao.Valor_Aposta");
            Main.economy.withdrawPlayer(p.getName(), Main.plugin.getConfig().getInt("Bolao.Valor_Aposta"));
            participantes.add(p);
            p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Bolao_Sucesso").replace("&", "§"));
        }
        return false;
    }

    public static void iniciar() {
        if (acontecendo)
            return;
        acontecendo = true;
        BukkitTask task = (new BukkitRunnable() {
            int anuncios = Main.plugin.getConfig().getInt("Bolao.Anuncios");

            public void run() {
                if (!Bolao.acontecendo) {
                    cancel();
                    return;
                }
                if (this.anuncios > 0) {
                    for (String msg : Main.plugin.getConfig().getStringList("Anuncios.Bolao_Aberto"))
                        Bukkit.broadcastMessage(msg.replace("&", "§").replace("@premio", "" + Bolao.aposta_total));
                    this.anuncios--;
                }
                if (this.anuncios == 0) {
                    if (Bolao.participantes.size() == 0) {
                        for (String msg : Main.plugin.getConfig().getStringList("Anuncios.Bolao_Cancelado"))
                            Bukkit.broadcastMessage(msg.replace("&", "§"));
                        Bolao.acontecendo = false;
                        cancel();
                        return;
                    }
                    Player ganhador = Bolao.participantes.get((new Random()).nextInt(Bolao.participantes.size()));
                    Main.economy.depositPlayer(ganhador.getName(), Bolao.aposta_total);
                    for (String msg : Main.plugin.getConfig().getStringList("Anuncios.Bolao_Ganhador"))
                        Bukkit.broadcastMessage(msg.replace("&", "§").replace("@premio", "" + Bolao.aposta_total).replace("@vencedor", ganhador.getName()));
                    Bolao.acontecendo = false;
                    Bolao.participantes.clear();
                    Bolao.aposta_total = 0;
                    cancel();
                }
            }
        }).runTaskTimer((Plugin)Main.plugin, 0L, (Main.plugin.getConfig().getInt("Bolao.Intervalo") * 20));
    }
}
