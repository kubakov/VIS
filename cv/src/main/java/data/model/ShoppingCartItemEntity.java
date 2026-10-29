package data.model;

import java.math.BigDecimal;

public class ShoppingCartItemEntity {
    private Long id;
    private ItemEntity item;
    private BigDecimal currPrice;

    public ShoppingCartItemEntity(Long id, ItemEntity item, BigDecimal currPrice) {
        this.id = id;
        this.item = item;
        this.currPrice = currPrice;
    }

    public Long getId() {
        return id;
    }

    public ItemEntity getItem() {
        return item;
    }

    public BigDecimal getCurrPrice() {
        return currPrice;
    }
}
