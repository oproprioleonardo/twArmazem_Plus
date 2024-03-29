package me.nullpointer.armazemplus.listeners;

import me.nullpointer.armazemplus.api.API;
import me.nullpointer.armazemplus.cache.DropC;
import me.nullpointer.armazemplus.cache.PlayerC;
import me.nullpointer.armazemplus.enums.DropType;
import me.nullpointer.armazemplus.enums.translate.ItemName;
import me.nullpointer.armazemplus.utils.Configuration;
import me.nullpointer.armazemplus.utils.Utils;
import me.nullpointer.armazemplus.utils.armazem.Armazem;
import me.nullpointer.armazemplus.utils.armazem.DropPlayer;
import me.nullpointer.armazemplus.utils.armazem.supliers.Drop;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlock implements Listener {

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        final Player p = e.getPlayer();
        final Block block = e.getBlock();
        final Armazem armazem = PlayerC.get(p.getName());
        for (DropPlayer dropPlayer : armazem.getDropPlayers()) {
            final Drop drop = DropC.get(dropPlayer.getKeyDrop());
            if (drop.getType().equals(DropType.BREAK) && drop.getDrop().isSimilar(block.getState().getData().toItemStack())) {
                final Configuration configuration = API.getConfiguration();
                if (!armazem.isMax()) {
                    final Double add = Math.floor(0D + block.getDrops().size() * Utils.getFortune(p) * armazem.getMultiplier());
                    if (armazem.isMax(add))
                        dropPlayer.addDropAmount(add - (armazem.getAmountAll() + add - armazem.getLimit()));
                    else dropPlayer.addDropAmount(add);
                    Utils.sendActionBar(p, configuration.getMessage("drops-add").replace("{amount}", Utils.format(add)).replace("{drop-type}", ItemName.valueOf(drop.getDrop()).getName()));
                } else Utils.sendActionBar(p, configuration.getMessage("armazem-max"));
                if (armazem.isAutoSell()) {
                    if (dropPlayer.getDropAmount() > 0) dropPlayer.sell(p.getPlayer(), armazem);
                }
                e.setCancelled(true);
                p.giveExp(e.getExpToDrop());
                block.setType(Material.AIR);
                return;
            }
        }
    }
}
