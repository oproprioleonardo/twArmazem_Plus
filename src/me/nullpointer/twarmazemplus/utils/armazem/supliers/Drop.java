package me.nullpointer.twarmazemplus.utils.armazem.supliers;

import me.nullpointer.twarmazemplus.enums.DropType;
import me.nullpointer.twarmazemplus.utils.Item;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class Drop {

    private String keyDrop;
    private DropType type;
    private ItemStack drop;
    private EntityType entityType;
    private Double unitPrice;
    private Item menuItem;

    public Drop(String keyDrop, DropType type, ItemStack drop, EntityType entityType, Double unitPrice, Item menuItem) {
        this.keyDrop = keyDrop;
        this.type = type;
        this.drop = drop;
        this.entityType = entityType;
        this.unitPrice = unitPrice;
        this.menuItem = menuItem;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getKeyDrop() {
        return keyDrop;
    }

    public DropType getType() {
        return type;
    }

    public ItemStack getDrop() {
        return drop;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Item getMenuItem() {
        return menuItem;
    }
}
