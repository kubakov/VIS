package data.mapper;

import data.model.*;
import domain.model.*;

import java.util.stream.Collectors;

public class OrderDataMapper {

    public static Order toDomain(OrderEntity entity) {
        if (entity == null) return null;

        Customer domainCustomer = new Customer(
                entity.getCustomer().getId(),
                entity.getCustomer().getfName(),
                entity.getCustomer().getlName(),
                entity.getCustomer().getPhone(),
                entity.getCustomer().getEmail()
        );

        Address domainAddress = new Address(
                entity.getAddress().getId(),
                entity.getAddress().getStreet(),
                entity.getAddress().getCity(),
                entity.getAddress().getHouseNumber(),
                entity.getAddress().getPostalCode()
        );

        Order domainOrder = new Order(entity.getId(), domainCustomer, domainAddress);

        if (entity.getItems() != null) {
            domainOrder.setItems(entity.getItems().stream()
                    .map(cartEntity -> new OrderItem(
                            ItemDataMapper.toDomain(cartEntity.getItem()),
                            cartEntity.getCurrPrice()
                    ))
                    .collect(Collectors.toList()));
        }

        return domainOrder;
    }

    public static OrderEntity toEntity(Order domain) {
        if (domain == null) return null;

        OrderEntity entity = new OrderEntity();
        entity.setId(domain.getId());

        if(domain.getCustomer() != null) {
            entity.setCustomer(new CustomerEntity(
                    domain.getCustomer().getId(),
                    domain.getCustomer().getFirstName(),
                    domain.getCustomer().getLastName(),
                    domain.getCustomer().getEmail(),
                    domain.getCustomer().getPhone()
            ));
        }

        if(domain.getDeliveryAddress() != null) {
            entity.setAddress(new AddressEntity(
                    domain.getDeliveryAddress().getId(),
                    domain.getDeliveryAddress().getStreet(),
                    domain.getDeliveryAddress().getCity(),
                    domain.getDeliveryAddress().getHouseNumber(),
                    domain.getDeliveryAddress().getPostalCode()
            ));
        }

        if(domain.getItems() != null) {
            entity.setItems(domain.getItems().stream()
                    .map(item -> new ShoppingCartItemEntity(
                                    null,
                                    ItemDataMapper.toEntity(item.getItem()),
                                    item.getPriceAtPurchase()
                    ))
                    .collect(Collectors.toList()));
        }
        return entity;
    }
}