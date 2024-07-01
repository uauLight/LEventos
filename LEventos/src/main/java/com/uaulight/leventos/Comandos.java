package com.uaulight.leventos;

import com.uaulight.leventos.Eventos.Bolao;
import com.uaulight.leventos.Eventos.Loteria;
import com.uaulight.leventos.Eventos.RandomVIP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Comandos implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("evento")) {
            if (!sender.hasPermission("leventos.admin")) {
                sender.sendMessage("§cVocê não possui permissão para isto.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(" ");
                sender.sendMessage("§e§lCOMANDOS DE EVENTOS:");
                sender.sendMessage("§7/evento bolao §8- §eInicia o evento Bolão.");
                sender.sendMessage("§7/evento loteria §8- §eInicia o evento Loteria.");
                sender.sendMessage("§7/evento randomvip §8- §eInicia o evento RandomVIP.");
                sender.sendMessage(" ");
                return true;
            }
            if (args[0].equalsIgnoreCase("bolao"))
                Bolao.iniciar();
            if (args[0].equalsIgnoreCase("loteria")) {
                Loteria.iniciar();
                sender.sendMessage("§aEvento iniciado com sucesso, número correto: §f" + Loteria.correto);
            }
            if (args[0].equalsIgnoreCase("randomvip"))
                RandomVIP.iniciar();
        }
        return false;
    }
}