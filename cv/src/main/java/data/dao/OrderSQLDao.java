package data.dao;

import data.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderSQLDao implements IOrderDao {

    private final Connection connection;

    public OrderSQLDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<OrderEntity> getOrderById(Long id) {
        String sql = """
            SELECT
                o.id AS order_id,
                c.id AS customer_id, c.fname, c.lname, c.phone, c.email,
                a.id AS address_id, a.street, a.city, a.house_number, a.postal_code,
                sc.id AS cart_id, sc.curr_price,
                i.id AS item_id, i.name AS item_name, i.description AS item_desc, i.price AS item_price
            FROM "order" o
            JOIN customer c ON o.customer_id = c.id
            JOIN address a ON o.address_id = a.id
            LEFT JOIN shopping_cart sc ON sc.order_id = o.id
            LEFT JOIN items i ON sc.item_id = i.id
            WHERE o.id = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                OrderEntity order = null;
                while (rs.next()) {
                    if (order == null) {
                        order = new OrderEntity();
                        order.setId(rs.getLong("order_id"));

                        CustomerEntity customer = new CustomerEntity(
                                rs.getLong("customer_id"),
                                rs.getString("fname"),
                                rs.getString("lname"),
                                rs.getString("email"),
                                rs.getString("phone")
                        );
                        order.setCustomer(customer);

                        AddressEntity address = new AddressEntity(
                                rs.getLong("address_id"),
                                rs.getString("street"),
                                rs.getString("city"),
                                rs.getString("house_number"),
                                rs.getString("postal_code")
                        );
                        order.setAddress(address);
                    }

                    Long cartId = rs.getObject("cart_id", Long.class);
                    if (cartId != null) {
                        ItemEntity item = new ItemEntity(
                                rs.getLong("item_id"),
                                rs.getString("item_name"),
                                rs.getString("item_desc"),
                                rs.getBigDecimal("item_price")
                        );
                        order.getItems().add(new ShoppingCartItemEntity(cartId, item, rs.getBigDecimal("curr_price")));
                    }
                }
                return Optional.ofNullable(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při načítání objednávky z DB", e);
        }
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        return new ArrayList<>();
    }

    @Override
    public OrderEntity createOrder(OrderEntity order) {
        String insertOrderSql = "INSERT INTO \"order\" (customer_id, address_id) VALUES (?, ?)";
        String insertCartSql = "INSERT INTO shopping_cart (order_id, item_id, curr_price) VALUES (?, ?, ?)";

        try {
            try (PreparedStatement ps = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, order.getCustomer().getId());
                ps.setLong(2, order.getAddress().getId());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setId(rs.getLong(1));
                    }
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(insertCartSql)) {
                for (ShoppingCartItemEntity item : order.getItems()) {
                    ps.setLong(1, order.getId());
                    ps.setLong(2, item.getItem().getId());
                    ps.setBigDecimal(3, item.getCurrPrice());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            return order;
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při ukládání objednávky", e);
        }
    }
}