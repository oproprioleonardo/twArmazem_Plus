package me.nullpointer.twarmazemplus.listeners;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import me.nullpointer.twarmazemplus.cache.DropCACHE;
import me.nullpointer.twarmazemplus.cache.PlayerCACHE;
import me.nullpointer.twarmazemplus.enums.DropType;
import me.nullpointer.twarmazemplus.utils.armazem.Armazem;
import me.nullpointer.twarmazemplus.utils.armazem.BoosterPlayer;
import me.nullpointer.twarmazemplus.utils.armazem.DropPlayer;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Drop;
import me.nullpointer.twstockexchange.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnItem implements Listener {

    @EventHandler
    public void spawn(ItemSpawnEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.m, () -> {
            try {
                final Item item = e.getEntity();
                final Plot plot = new PlotAPI().getPlot(item.getLocation());
                if (plot.isBasePlot() && plot.hasOwner()) {
                    final ItemStack itemStack = item.getItemStack();
                    final OfflinePlayer p = Bukkit.getOfflinePlayer(plot.getOwners().stream().findFirst().get());
                    if (!p.isOnline()) return;
                    final Armazem armazem = PlayerCACHE.get(p.getName());
                    for (DropPlayer dropPlayer : armazem.getDropPlayers()) {
                        final Drop drop = DropCACHE.get(dropPlayer.getKeyDrop());
                        if (drop.getType().equals(DropType.BREAK) && drop.getDrop().isSimilar(itemStack)) {
                            if (!armazem.isMax()) {
                                Double multiplier = armazem.getMultiplier();
                                for (BoosterPlayer boosterPlayer : armazem.getBoostersActive()) {
                                    multiplier += boosterPlayer.getMultiplier();
                                }
                                final Double add = Math.floor(0D + itemStack.getAmount() * multiplier);
                                if (armazem.isMax(add))
                                    dropPlayer.addDropAmount(add - (armazem.getAmountAll() + add - armazem.getLimit()));
                                else dropPlayer.addDropAmount(add);
                            }
                            e.setCancelled(true);
                            item.remove();
                            return;
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        });
    }

}
