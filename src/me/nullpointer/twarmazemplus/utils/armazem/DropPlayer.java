package me.nullpointer.twarmazemplus.utils.armazem;

import me.nullpointer.twarmazemplus.Main;
import me.nullpointer.twarmazemplus.cache.DropC;
import me.nullpointer.twarmazemplus.utils.armazem.supliers.Drop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DropPlayer {

    private String keyDrop;
    private Double amount;

    public DropPlayer(String keyDrop, Double amount) {
        this.keyDrop = keyDrop;
        this.amount = amount;
    }

    public String getKeyDrop() {
        return keyDrop;
    }

    public Double getDropAmount() {
        return amount;
    }

    public void setDropAmount(Double amount) {
        this.amount = amount;
    }

    public void sell(Player p) {
        final Drop drop = DropC.get(getKeyDrop());
        final Economy economy = Main.economy;
        economy.depositPlayer(p, drop.getUnitPrice() * getDropAmount());
        setDropAmount(0D);
    }

    public void addDropAmount(Double amount){
        this.amount += amount;
    }

    public void removeDropAmount(Double amount){
        this.amount -= amount;
    }
}
