package me.nullpointer.twarmazemplus.listeners;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import me.nullpointer.twarmazemplus.Main;
import me.nullpointer.twarmazemplus.api.API;
import me.nullpointer.twarmazemplus.cache.DropC;
import me.nullpointer.twarmazemplus.cache.PlayerC;
import me.nullpointer.twarmazemplus.enums.DropType;
import me.nullpointer.twarmazemplus.utils.armazem.Armazem;
import me.nullpointer.twarmazemplus.utils.armazem.BoosterPlayer;
import me.nullpointer.twarmazemplus.utils.armazem.DropPlayer;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Drop;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

public class ChunkLoad implements Listener {

    @EventHandler
    public void load(ChunkLoadEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try{
                if (e.getWorld().getName().equalsIgnoreCase(API.getSettings().getWorldPlot())) {
                    for (Entity entity : e.getChunk().getEntities()){
                        if (!entity.getType().equals(EntityType.DROPPED_ITEM)) continue;
                        final Item item = (Item) entity;
                        final Plot plot = new PlotAPI().getPlot(item.getLocation());
                        if (plot.isBasePlot() && plot.hasOwner()) {
                            final ItemStack itemStack = item.getItemStack();
                            final OfflinePlayer p = Bukkit.getOfflinePlayer(plot.getOwners().stream().findFirst().get());
                            if (!p.isOnline()) return;
                            final Armazem armazem = PlayerC.get(p.getName());
                            for (DropPlayer dropPlayer : armazem.getDropPlayers()) {
                                final Drop drop = DropC.get(dropPlayer.getKeyDrop());
                                if (drop.getType().equals(DropType.PLOT_DROP) && drop.getDrop().isSimilar(itemStack)) {
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
                                    item.remove();
                                    return;
                                }
                            }
                        }
                    }
                }
            }catch (Exception ignored){
            }
        });

    }
}
