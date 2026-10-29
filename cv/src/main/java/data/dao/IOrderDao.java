package data.dao;

import data.model.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface IOrderDao {
    Optional<OrderEntity> getOrderById(Long id);
    List<OrderEntity> getAllOrders();
    OrderEntity createOrder(OrderEntity order);
}
