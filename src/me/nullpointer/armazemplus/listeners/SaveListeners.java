package me.nullpointer.armazemplus.listeners;

import me.nullpointer.armazemplus.cache.PlayerC;
import me.nullpointer.armazemplus.cache.temporary.CollectC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SaveListeners implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        PlayerC.load(e.getPlayer());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        PlayerC.remove(e.getPlayer().getName());
        if (CollectC.has(e.getPlayer())) CollectC.remove(e.getPlayer());
    }
}
