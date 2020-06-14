package me.nullpointer.armazemplus.utils.armazem;

import me.nullpointer.armazemplus.Main;
import me.nullpointer.armazemplus.cache.DropC;
import me.nullpointer.armazemplus.utils.armazem.supliers.Drop;
import net.milkbowl.vault.economy.Economy;
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

    public void sell(Player p, Armazem armazem) {
        final Drop drop = DropC.get(getKeyDrop());
        final Economy economy = Main.economy;
        final double value = drop.getUnitPrice() * getDropAmount();
        economy.depositPlayer(p, value + (value * armazem.getBonus() / 100));
        setDropAmount(0D);
    }

    public void addDropAmount(Double amount) {
        this.amount += amount;
    }

    public void removeDropAmount(Double amount) {
        this.amount -= amount;
    }
}
