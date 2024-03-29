package me.nullpointer.armazemplus.listeners;

import me.nullpointer.armazemplus.api.API;
import me.nullpointer.armazemplus.cache.DropC;
import me.nullpointer.armazemplus.cache.PlayerC;
import me.nullpointer.armazemplus.enums.DropType;
import me.nullpointer.armazemplus.enums.StackMob;
import me.nullpointer.armazemplus.enums.translate.ItemName;
import me.nullpointer.armazemplus.utils.Configuration;
import me.nullpointer.armazemplus.utils.Settings;
import me.nullpointer.armazemplus.utils.Utils;
import me.nullpointer.armazemplus.utils.armazem.Armazem;
import me.nullpointer.armazemplus.utils.armazem.DropPlayer;
import me.nullpointer.armazemplus.utils.armazem.supliers.Drop;
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
                        final Double add = Math.floor(Utils.multiplyDrops(p, getMobAmount(mob)) * armazem.getMultiplier());
                        if (armazem.isMax(add))
                            dropPlayer.addDropAmount(add - (armazem.getAmountAll() + add - armazem.getLimit()));
                        else dropPlayer.addDropAmount(add);
                        Utils.sendActionBar(p, configuration.getMessage("drops-add").replace("{amount}", Utils.format(add)).replace("{drop-type}", ItemName.valueOf(drop.getDrop()).getName()));
                    } else Utils.sendActionBar(p, configuration.getMessage("armazem-max"));
                    if (armazem.isAutoSell()) {
                        if (dropPlayer.getDropAmount() > 0) dropPlayer.sell(p.getPlayer(), armazem);
                    }
                    e.getDrops().clear();
                    return;
                }
            }
        }
    }
}
