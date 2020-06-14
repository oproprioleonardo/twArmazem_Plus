package me.nullpointer.armazemplus.listeners;

import me.nullpointer.armazemplus.api.API;
import me.nullpointer.armazemplus.cache.BoosterC;
import me.nullpointer.armazemplus.cache.LimitsC;
import me.nullpointer.armazemplus.cache.PlayerC;
import me.nullpointer.armazemplus.utils.Configuration;
import me.nullpointer.armazemplus.utils.FormatTime;
import me.nullpointer.armazemplus.utils.Settings;
import me.nullpointer.armazemplus.utils.Utils;
import me.nullpointer.armazemplus.utils.armazem.Armazem;
import me.nullpointer.armazemplus.utils.armazem.BoosterPlayer;
import me.nullpointer.armazemplus.utils.armazem.supliers.Booster;
import me.nullpointer.armazemplus.utils.armazem.supliers.Limit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class Interact implements Listener {

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (e.getItem() == null) return;
        final ItemStack item = e.getItem();
        for (Limit limit : LimitsC.limits) {
            if (!item.isSimilar(limit.getItemStack())) continue;
            final Settings settings = API.getSettings();
            final Armazem armazem = PlayerC.get(p.getName());
            final Configuration configuration = API.getConfiguration();
            if (armazem.getLimit() >= settings.getLimitMax()) {
                p.sendMessage(configuration.getMessage("limit-max"));
                return;
            }
            if (!limit.hasPermission(p)) {
                p.sendMessage(configuration.getMessage("permission-error-limit"));
                return;
            }
            Double adder;
            if (armazem.getLimit() + limit.getValue() > settings.getLimitMax())
                adder = limit.getValue() - (armazem.getLimit() + limit.getValue() - settings.getLimitMax());
            else adder = limit.getValue();
            armazem.addLimit(adder);
            if (item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
            else p.setItemInHand(new ItemStack(Material.AIR));
            p.sendMessage(configuration.getMessage("limit-used").replace("{limit-value}", Utils.format(adder)).replace("{player-limit-value}", Utils.format(armazem.getLimit())));
            e.setCancelled(true);
            return;
        }
        for (Booster booster : BoosterC.boosters) {
            if (!item.isSimilar(booster.getItem())) continue;
            final Armazem armazem = PlayerC.get(p.getName());
            final Configuration configuration = API.getConfiguration();
            if (!booster.hasPermission(p)) {
                p.sendMessage(configuration.getMessage("permission-error-booster"));
                return;
            }
            if (booster.getTime() == 0) {
                armazem.addMultiplier(booster.getMultiplier());
                p.sendMessage(configuration.getMessage("booster-used").replace("{time}", "infinito").replace("{multiplier}", booster.getMultiplier().toString()));
            } else {
                armazem.addBooster(new BoosterPlayer(booster.getMultiplier(), booster.getTime()));
                p.sendMessage(configuration.getMessage("booster-used").replace("{time}", new FormatTime(TimeUnit.SECONDS.toMillis(booster.getTime())).format()).replace("{multiplier}", booster.getMultiplier().toString()));
            }
            if (item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
            else p.setItemInHand(new ItemStack(Material.AIR));
            e.setCancelled(true);
            return;
        }
    }
}
