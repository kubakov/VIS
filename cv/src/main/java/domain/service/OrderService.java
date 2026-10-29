package domain.service;

import data.dao.IOrderDao;
import data.mapper.OrderDataMapper;
import data.model.OrderEntity;
import db.GlobalConn;
import domain.model.Order;

import java.util.Optional;

public class OrderService implements IOrderService {

    @Override
    public Optional<Order> getOrderById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID objednávky");
        }
        IOrderDao dao = GlobalConn.getOrderDao();
        return dao.getOrderById(id).map(OrderDataMapper::toDomain);
    }

    @Override
    public Order createOrder(Order order) {
        if (order == null || !order.isValid()) {
            throw new IllegalArgumentException("Objednávka je neplatná (musí obsahovat zákazníka, adresu a alespoň jednu položku).");
        }
        IOrderDao dao = GlobalConn.getOrderDao();
        if(dao == null) {
            throw new UnsupportedOperationException("Zvolené úložiště nepodporuje ukládání objednávek.");
        }
        OrderEntity savedEntity = dao.createOrder(OrderDataMapper.toEntity(order));
        order.setId(savedEntity.getId());
        return order;
    }
}