package me.nullpointer.twarmazemplus.utils.armazem;

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

    public void addDropAmount(Double amount){
        this.amount += amount;
    }

    public void removeDropAmount(Double amount){
        this.amount -= amount;
    }
}
