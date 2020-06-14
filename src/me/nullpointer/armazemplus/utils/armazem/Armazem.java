package me.nullpointer.armazemplus.utils.armazem;

import me.nullpointer.armazemplus.Main;
import me.nullpointer.armazemplus.cache.DropC;
import me.nullpointer.armazemplus.utils.armazem.supliers.Drop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Armazem {

    private String owner;
    private Double limit;
    private Double multiplier;
    private boolean autoSell = false;
    private Integer bonus = 0;
    private List<BoosterPlayer> boostersActive;
    private List<DropPlayer> dropPlayers;
    private List<String> friends;

    public Armazem(String owner, Double limit, Double multiplier, List<BoosterPlayer> boostersActive, List<DropPlayer> dropPlayers, List<String> friends, Integer bonus) {
        this.owner = owner;
        this.limit = limit;
        this.multiplier = multiplier;
        this.boostersActive = boostersActive;
        this.dropPlayers = dropPlayers;
        this.friends = friends;
        this.bonus = bonus;
    }

    public Integer getBonus() {
        return bonus;
    }

    public boolean isAutoSell() {
        return autoSell;
    }

    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;
    }

    public String isAutoSellResult() {
        return isAutoSell() ? "ativado" : "desativado";
    }

    public Double getAmountAll() {
        Double amountAll = 0D;
        for (DropPlayer dropPlayer : getDropPlayers()) {
            amountAll += dropPlayer.getDropAmount();
        }
        return amountAll;
    }


    public Double getPriceAll() {
        double priceAll = 0D;
        for (DropPlayer dropPlayer : dropPlayers) {
            final Drop drop = DropC.get(dropPlayer.getKeyDrop());
            priceAll += drop.getUnitPrice() * dropPlayer.getDropAmount();
        }
        return priceAll;
    }

    public boolean isMax() {
        return getAmountAll() >= getLimit();
    }

    public boolean isMax(Double amount) {
        return getAmountAll() + amount > getLimit();
    }

    public String getOwner() {
        return owner;
    }

    public Player getOwnerPlayer() {
        return Bukkit.getPlayer(owner);
    }

    public String getCorrectNameOwner() {
        return Bukkit.getOfflinePlayer(owner).getName();
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public Double getMultiplier() {
        return multiplier + getMultiplierBoosters();
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public Double getOriginalMultiplier() {
        return multiplier;
    }

    public void addMultiplier(Double multiplier) {
        this.multiplier += multiplier;
    }

    public Double getMultiplierBoosters() {
        Double value = 0D;
        for (BoosterPlayer boosterPlayer : getBoostersActive()) {
            value += boosterPlayer.getMultiplier();
        }
        return value;
    }

    public List<BoosterPlayer> getBoostersActive() {
        return boostersActive;
    }

    public void setBoostersActive(List<BoosterPlayer> boostersActive) {
        this.boostersActive = boostersActive;
    }

    public void addBooster(BoosterPlayer boosterPlayer) {
        getBoostersActive().add(boosterPlayer);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            final ArrayList<BoosterPlayer> list = new ArrayList<>(getBoostersActive());
            list.remove(boosterPlayer);
            setBoostersActive(list);
        }, 20 * boosterPlayer.getTime());
    }

    public List<DropPlayer> getDropPlayers() {
        return dropPlayers;
    }

    public void setDropPlayers(List<DropPlayer> dropPlayers) {
        this.dropPlayers = dropPlayers;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void removeLimit(Double limit) {
        this.limit -= limit;
    }

    public void addLimit(Double limit) {
        this.limit += limit;
    }
}
