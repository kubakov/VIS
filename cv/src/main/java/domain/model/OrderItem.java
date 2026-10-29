package domain.model;

import java.math.BigDecimal;

public class OrderItem {
    private Item item;
    private BigDecimal priceAtPurchase;

    public OrderItem(Item item, BigDecimal priceAtPurchase) {
        this.item = item;
        this.priceAtPurchase = priceAtPurchase;
    }

    public Item getItem() { return item; }
    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
}