package me.nullpointer.armazemplus.utils.armazem.supliers;

import me.nullpointer.armazemplus.enums.DropType;
import me.nullpointer.armazemplus.utils.Item;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class Drop {

    private final String keyDrop;
    private final DropType type;
    private final ItemStack drop;
    private final EntityType entityType;
    private final Double unitPrice;
    private final Item menuItem;
    private final boolean canCollect;

    public Drop(String keyDrop, DropType type, ItemStack drop, EntityType entityType, Double unitPrice, Item menuItem, boolean canCollect) {
        this.keyDrop = keyDrop;
        this.type = type;
        this.drop = drop;
        this.entityType = entityType;
        this.unitPrice = unitPrice;
        this.menuItem = menuItem;
        this.canCollect = canCollect;
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

    public boolean isCanCollect() {
        return canCollect;
    }
}
