package data.model;

import java.util.ArrayList;
import java.util.List;

public class OrderEntity {
    private Long id;
    private CustomerEntity customer;
    private AddressEntity address;
    private List<ShoppingCartItemEntity> items = new ArrayList<>();

    public OrderEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public List<ShoppingCartItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItemEntity> items) {
        this.items = items;
    }
}
