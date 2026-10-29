package domain.service;

import domain.model.Order;
import java.util.Optional;

public interface IOrderService {
    Optional<Order> getOrderById(Long id);
    Order createOrder(Order order);
}