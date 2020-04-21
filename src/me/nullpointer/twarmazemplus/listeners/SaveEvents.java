package me.nullpointer.twarmazemplus.listeners;

import me.nullpointer.twarmazemplus.cache.PlayerCACHE;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SaveEvents implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        PlayerCACHE.load(e.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e){
        PlayerCACHE.remove(e.getPlayer().getName());
    }
}
