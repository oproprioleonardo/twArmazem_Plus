package me.nullpointer.armazemplus.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class AntiLag implements Listener {

    @EventHandler
    public void debug(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player || e.getEntity().getType() == EntityType.ARMOR_STAND) {
            return;
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause() == EntityDamageEvent.DamageCause.VOID || e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void damage(EntityDamageByBlockEvent e) {
        e.setCancelled(true);
    }
}
