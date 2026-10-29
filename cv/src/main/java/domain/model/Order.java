package domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Customer customer;
    private Address deliveryAddress;
    private List<OrderItem> items = new ArrayList<>();

    public Order(Long id, Customer customer, Address deliveryAddress) {
        this.id = id;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
    }

    public Order(Customer customer, Address deliveryAddress, List<OrderItem> items) {
        this(null, customer, deliveryAddress);
        this.items = (items == null) ? new ArrayList<>() : items;
    }

    public BigDecimal calculateTotalPrice() {
        return items.stream()
                .map(OrderItem::getPriceAtPurchase)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isValid(){
        return customer != null && deliveryAddress != null && items != null && !items.isEmpty();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public Address getDeliveryAddress() { return deliveryAddress; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}