package me.nullpointer.armazemplus.listeners;

import me.nullpointer.armazemplus.api.API;
import me.nullpointer.armazemplus.cache.DropC;
import me.nullpointer.armazemplus.cache.PlayerC;
import me.nullpointer.armazemplus.cache.temporary.CollectC;
import me.nullpointer.armazemplus.utils.Configuration;
import me.nullpointer.armazemplus.utils.Utils;
import me.nullpointer.armazemplus.utils.armazem.Armazem;
import me.nullpointer.armazemplus.utils.armazem.DropPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class TalkToCollect implements Listener {

    @EventHandler
    public void talk(AsyncPlayerChatEvent e){
        final Player p = e.getPlayer();
        final String message = e.getMessage();
        final Configuration configuration = API.getConfiguration();
        if (CollectC.has(p)) return;
        e.setCancelled(true);
        final ItemStack drop = CollectC.getDrop(p);
        if (message.equalsIgnoreCase("cancelar")){
            p.sendMessage(configuration.getMessage("cancel"));
            CollectC.remove(p);
            return;
        }
        try {
            final Double value = Double.parseDouble(message);
            if (value <= 0) p.sendMessage(configuration.getMessage("number-invalid"));
            else {
                final Armazem armazem = PlayerC.get(p.getName());
                final DropPlayer dropPlayer = armazem.getDropPlayers().stream().filter(dropPlayer1 -> DropC.get(dropPlayer1.getKeyDrop()).getDrop().isSimilar(drop)).findFirst().get();
                if (dropPlayer.getDropAmount() < value){
                    p.sendMessage(configuration.getMessage("drop-insufficient"));
                    return;
                }
                if (!Utils.haveSpace(p.getInventory(), drop, value.intValue())){
                    p.sendMessage(configuration.getMessage("space-insufficient"));
                    return;
                }
                dropPlayer.removeDropAmount(value);
                Utils.giveItem(p, drop, value.intValue());
                CollectC.remove(p);
            }
        }catch (final Exception exception){
            CollectC.remove(p);
            p.sendMessage(configuration.getMessage("number-invalid"));
        }



    }

}
