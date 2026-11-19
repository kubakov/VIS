package domain.service;

import data.mapper.ItemDataMapper;
import data.mapper.OrderDataMapper;
import data.model.*;
import db.GlobalConn;
import domain.model.Order;
import uow.IUnitOfWork;

import java.util.Optional;
import java.util.stream.Collectors;

public class OrderService implements IOrderService {

    @Override
    public Optional<Order> getOrderById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID objednávky");
        }
        try (IUnitOfWork uow = GlobalConn.getFactory().createUnitOfWork()) {
            return uow.getOrderDao().getOrderById(id).map(OrderDataMapper::toDomain);
        }
    }

    @Override
    public Order createOrder(Order order) {
        if (order == null || !order.isValid()) {
            throw new IllegalArgumentException("Objednávka je neplatná (musí obsahovat zákazníka, adresu a alespoň jednu položku).");
        }

        try (IUnitOfWork uow = GlobalConn.getFactory().createUnitOfWork()) {

            AddressEntity addressEntity = new AddressEntity(
                    null,
                    order.getDeliveryAddress().getStreet(),
                    order.getDeliveryAddress().getCity(),
                    order.getDeliveryAddress().getHouseNumber(),
                    order.getDeliveryAddress().getPostalCode()
            );
            addressEntity = uow.getAddressDao().create(addressEntity);

            CustomerEntity customerEntity = new CustomerEntity(
                    null,
                    order.getCustomer().getFirstName(),
                    order.getCustomer().getLastName(),
                    order.getCustomer().getEmail(),
                    order.getCustomer().getPhone()
            );
            customerEntity = uow.getCustomerDao().create(customerEntity);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setCustomer(customerEntity);
            orderEntity.setAddress(addressEntity);
            orderEntity.setItems(order.getItems().stream()
                    .map(item -> new ShoppingCartItemEntity(
                            null,
                            ItemDataMapper.toEntity(item.getItem()),
                            item.getPriceAtPurchase()
                    ))
                    .collect(Collectors.toList()));

            OrderEntity savedOrder = uow.getOrderDao().createOrder(orderEntity);
            order.setId(savedOrder.getId());

            uow.commit();
            return order;
        }
    }
}