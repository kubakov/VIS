package data.dao;

import data.identitymap.CustomerIdentityMap;
import data.model.CustomerEntity;
import java.sql.*;
import java.util.Optional;

public class CustomerSQLDao implements ICustomerDao {

    private final Connection connection;
    private final CustomerIdentityMap identityMap;

    public CustomerSQLDao(Connection connection) {
        this.connection = connection;
        this.identityMap = new CustomerIdentityMap();
    }

    public CustomerSQLDao(Connection connection, CustomerIdentityMap identityMap) {
        this.connection = connection;
        this.identityMap = identityMap;
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
            identityMap.put(customer.getId(), customer);
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při ukládání zákazníka", e);
        }
    }

    @Override
    public Optional<CustomerEntity> getById(Long id) {
        Optional<CustomerEntity> cached = identityMap.get(id);
        if(cached.isPresent()) {
            return cached;
        }

        String sql = "SELECT id, fname, lname, email, phone FROM customer WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CustomerEntity customer = new CustomerEntity(
                            rs.getLong("id"),
                            rs.getString("fname"),
                            rs.getString("lname"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                    identityMap.put(customer.getId(), customer);
                    return Optional.of(customer);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při čtení zákazníka", e);
        }
    }
}