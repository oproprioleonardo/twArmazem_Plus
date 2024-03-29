package me.nullpointer.armazemplus.commands;

import me.nullpointer.armazemplus.api.API;
import me.nullpointer.armazemplus.cache.LimitsC;
import me.nullpointer.armazemplus.cache.PlayerC;
import me.nullpointer.armazemplus.utils.Configuration;
import me.nullpointer.armazemplus.utils.Utils;
import me.nullpointer.armazemplus.utils.armazem.Armazem;
import me.nullpointer.armazemplus.utils.armazem.supliers.Limit;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CmdLimit extends BukkitCommand {
    public CmdLimit() {
        super("limitedevenda", "Comando de limite de vendas", "/limite ajuda", Arrays.asList("limit", "limites"));
    }

    @Override
    public boolean execute(CommandSender s, String lbl, String[] args) {
        if (args.length == 0) {
            if (s instanceof Player) {
                final Player p = (Player) s;
                final Armazem armazem = PlayerC.get(p.getName());
                p.sendMessage(API.getConfiguration().getMessage("limit-view").replace("{limit}", Utils.format(armazem.getLimit())));
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("ajuda") || args[0].equalsIgnoreCase("help")) {
                s.sendMessage("§aComandos Armazém - Sistema de limite de venda");
                s.sendMessage("");
                s.sendMessage("§a/limitedevenda - Mostra seu limite de venda atual.");
                s.sendMessage("§a/limitedevenda ajuda - Mostra os comandos do sistema.");
                if (s.hasPermission("armazem.admin")) s.sendMessage("§a/limitedevenda give (player) (id) (amount)");
            } else Bukkit.dispatchCommand(s, "limitedevenda ajuda");
        } else if (args.length == 4) {
            final Configuration configuration = API.getConfiguration();
            if (args[0].equalsIgnoreCase("give")) {
                if (!s.hasPermission("armazem.admin")) {
                    s.sendMessage(configuration.getMessage("permission-error"));
                    return true;
                }
                if (Bukkit.getPlayer(args[1]) == null) {
                    s.sendMessage(configuration.getMessage("player-offline"));
                    return true;
                }
                for (Limit limit : LimitsC.limits) {
                    if (limit.getKey().equalsIgnoreCase(args[2])) {
                        if (StringUtils.isNumeric(args[3])) {
                            final int number = Integer.parseInt(args[3]);
                            s.sendMessage(configuration.getMessage("limit-gived").replace("{type}", limit.getKey()).replace("{player}", args[1]));
                            final Player player = Bukkit.getPlayer(args[1]);
                            Utils.giveItem(player, limit.getItemStack().clone(), number);
                            return true;
                        } else s.sendMessage(configuration.getMessage("number-invalid"));
                        return true;
                    }
                }
                s.sendMessage(configuration.getMessage("limit-not-found"));
            } else Bukkit.dispatchCommand(s, "limitedevenda ajuda");
        }
        return true;
    }
}
