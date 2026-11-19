package data.dao;

import data.model.CustomerEntity;
import java.sql.*;
import java.util.Optional;

public class CustomerSQLDao implements ICustomerDao {

    private final Connection connection;

    public CustomerSQLDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CustomerEntity create(CustomerEntity customer) {
        String sql = "INSERT INTO customer (fname, lname, email, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer.getfName());
            ps.setString(2, customer.getlName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    customer.setId(rs.getLong(1));
                }
            }
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při ukládání zákazníka", e);
        }
    }

    @Override
    public Optional<CustomerEntity> getById(Long id) {
        String sql = "SELECT id, fname, lname, email, phone FROM customer WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CustomerEntity(
                            rs.getLong("id"),
                            rs.getString("fname"),
                            rs.getString("lname"),
                            rs.getString("email"),
                            rs.getString("phone")
                    ));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při čtení zákazníka", e);
        }
    }
}