package me.nullpointer.twarmazemplus.commands;

import me.nullpointer.twarmazemplus.api.API;
import me.nullpointer.twarmazemplus.cache.BoosterC;
import me.nullpointer.twarmazemplus.cache.PlayerC;
import me.nullpointer.twarmazemplus.utils.Configuration;
import me.nullpointer.twarmazemplus.utils.Utils;
import me.nullpointer.twarmazemplus.utils.armazem.Armazem;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Booster;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CmdBooster extends BukkitCommand {


    public CmdBooster() {
        super("booster", "Comando booster", "/booster ajuda", Arrays.asList("boosters", "bs", "multiplicador"));
    }

    @Override
    public boolean execute(CommandSender s, String lbl, String[] args) {
        if (args.length == 0) {
            if (s instanceof Player) {
                final Player p = (Player) s;
                final Armazem armazem = PlayerC.get(p.getName());
                p.sendMessage(API.getConfiguration().getMessage("booster-view").replace("{multiplier}", armazem.getMultiplier().toString()));
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("ajuda") || args[0].equalsIgnoreCase("help")) {
                s.sendMessage("§aComandos Armazém - Sistema de Boosters");
                s.sendMessage("");
                s.sendMessage("§a/booster - Mostra seu multiplicador atual.");
                s.sendMessage("§a/booster ajuda - Mostra os comandos do sistema.");
                if (s.hasPermission("armazem.admin")) s.sendMessage("§a/booster give (player) (id) (amount)");
            } else Bukkit.dispatchCommand(s, "booster ajuda");
        } else if (args.length == 4) {
            final Configuration configuration = API.getConfiguration();
            if (args[0].equalsIgnoreCase("give")) {
                if (!s.hasPermission("armazem.admin")){
                    s.sendMessage(configuration.getMessage("permission-error"));
                    return true;
                }
                if (Bukkit.getPlayer(args[1]) == null) {
                    s.sendMessage(configuration.getMessage("player-offline"));
                    return true;
                }
                for (Booster booster : BoosterC.boosters) {
                    if (booster.getKey().equalsIgnoreCase(args[2])) {
                        if (StringUtils.isNumeric(args[3])) {
                            final int number = Integer.parseInt(args[3]);
                            s.sendMessage(configuration.getMessage("booster-gived").replace("{type}", booster.getKey()).replace("{player}", args[1]));
                            final Player player = Bukkit.getPlayer(args[1]);
                            Utils.giveItem(player, booster.getItem().clone(), number);
                            return true;
                        } else s.sendMessage(configuration.getMessage("number-invalid"));
                        return true;
                    }
                }
                s.sendMessage(configuration.getMessage("booster-not-found"));
            } else Bukkit.dispatchCommand(s, "booster ajuda");
        }
        return true;
    }
}
