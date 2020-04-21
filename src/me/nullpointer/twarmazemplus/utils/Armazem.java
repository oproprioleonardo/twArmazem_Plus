package me.nullpointer.twarmazemplus.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Armazem {

    private String owner;
    private Double limit;
    private Double multiplier;
    private List<BoosterPlayer> boostersActive;
    private List<DropPlayer> dropPlayers;
    private List<String> friends;

    public Armazem(String owner, Double limit, Double multiplier, List<BoosterPlayer> boostersActive, List<DropPlayer> dropPlayers, List<String> friends) {
        this.owner = owner;
        this.limit = limit;
        this.multiplier = multiplier;
        this.boostersActive = boostersActive;
        this.dropPlayers = dropPlayers;
        this.friends = friends;
    }

    public String getOwner() {
        return owner;
    }

    public Player getOwnerPlayer(){
        return Bukkit.getPlayer(owner);
    }

    public String getCorrectNameOwner(){
        return Bukkit.getOfflinePlayer(owner).getName();
    }

    public Double getLimit() {
        return limit;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public List<BoosterPlayer> getBoostersActive() {
        return boostersActive;
    }

    public void setBoostersActive(List<BoosterPlayer> boostersActive) {
        this.boostersActive = boostersActive;
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

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public void removeLimit(Double limit){
        this.limit -= limit;
    }

    public void addLimit(Double limit){
        this.limit += limit;
    }
}
