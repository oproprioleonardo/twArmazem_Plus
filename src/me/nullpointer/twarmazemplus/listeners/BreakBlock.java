package me.nullpointer.twarmazemplus.listeners;

import me.nullpointer.twarmazemplus.cache.DropCACHE;
import me.nullpointer.twarmazemplus.cache.PlayerCACHE;
import me.nullpointer.twarmazemplus.enums.DropType;
import me.nullpointer.twarmazemplus.utils.Utils;
import me.nullpointer.twarmazemplus.utils.armazem.Armazem;
import me.nullpointer.twarmazemplus.utils.armazem.BoosterPlayer;
import me.nullpointer.twarmazemplus.utils.armazem.DropPlayer;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Drop;
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
        final Armazem armazem = PlayerCACHE.get(p.getName());
        for (DropPlayer dropPlayer : armazem.getDropPlayers()) {
            final Drop drop = DropCACHE.get(dropPlayer.getKeyDrop());
            if (drop.getType().equals(DropType.BREAK) && drop.getDrop().isSimilar(block.getState().getData().toItemStack())) {
                if (!armazem.isMax()){
                    Double multiplier = armazem.getMultiplier();
                    for (BoosterPlayer boosterPlayer : armazem.getBoostersActive()) { multiplier += boosterPlayer.getMultiplier(); }
                    final Double add = Math.floor(0D + block.getDrops().size() * Utils.getFortune(p) * multiplier);
                    if (armazem.isMax(add)) dropPlayer.addDropAmount(add - (armazem.getAmountAll() + add - armazem.getLimit()));
                    else dropPlayer.addDropAmount(add);
                }
                e.setCancelled(true);
                p.giveExp(e.getExpToDrop());
                block.setType(Material.AIR);
                return;
            }
        }
    }
}
