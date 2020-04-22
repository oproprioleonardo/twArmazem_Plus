package me.nullpointer.twarmazemplus.listeners;

import me.nullpointer.twarmazemplus.api.API;
import me.nullpointer.twarmazemplus.cache.DropC;
import me.nullpointer.twarmazemplus.cache.PlayerC;
import me.nullpointer.twarmazemplus.enums.DropType;
import me.nullpointer.twarmazemplus.enums.StackMob;
import me.nullpointer.twarmazemplus.enums.translate.ItemName;
import me.nullpointer.twarmazemplus.utils.Configuration;
import me.nullpointer.twarmazemplus.utils.Settings;
import me.nullpointer.twarmazemplus.utils.Utils;
import me.nullpointer.twarmazemplus.utils.armazem.Armazem;
import me.nullpointer.twarmazemplus.utils.armazem.BoosterPlayer;
import me.nullpointer.twarmazemplus.utils.armazem.DropPlayer;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Drop;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillMob implements Listener {

    public static Double getMobAmount(Entity entity) {
        final Settings settings = API.getSettings();
        if (settings.getStackMob().equals(StackMob.StackMobs)) {
            if (entity.hasMetadata("JH_StackMobs")) {
                return 0D + entity.getMetadata("JH_StackMobs").get(0).asInt();
            }
        }
        if (settings.getStackMob().equals(StackMob.JH_StackMobs)) {
            if (entity.hasMetadata("stackmob:stack-size")) {
                return 0D + entity.getMetadata("stackmob:stack-size").get(0).asInt();
            }
        }
        return 1D;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void kill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            final Player p = e.getEntity().getKiller();
            final Entity mob = e.getEntity();
            final Armazem armazem = PlayerC.get(p.getName());
            for (DropPlayer dropPlayer : armazem.getDropPlayers()) {
                final Drop drop = DropC.get(dropPlayer.getKeyDrop());
                if (drop.getType().equals(DropType.KILL) && drop.getEntityType().equals(mob.getType())) {
                    final Configuration configuration = API.getConfiguration();
                    if (!armazem.isMax()) {
                        Double multiplier = armazem.getMultiplier();
                        for (BoosterPlayer boosterPlayer : armazem.getBoostersActive()) {
                            multiplier += boosterPlayer.getMultiplier();
                        }
                        final Double add = Math.floor(Utils.multiplyDrops(p, getMobAmount(mob)) * multiplier);
                        if (armazem.isMax(add))
                            dropPlayer.addDropAmount(add - (armazem.getAmountAll() + add - armazem.getLimit()));
                        else dropPlayer.addDropAmount(add);
                        Utils.sendActionBar(p, configuration.getMessage("drops-add").replace("{amount}", Utils.format(add)).replace("{drop-type}", ItemName.valueOf(drop.getDrop()).getName()));
                    }else Utils.sendActionBar(p, configuration.getMessage("armazem-max"));
                    e.getDrops().clear();
                    return;
                }
            }
        }
    }
}
