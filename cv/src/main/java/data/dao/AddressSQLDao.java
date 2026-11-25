package data.dao;

import data.identitymap.AddressIdentityMap;
import data.model.AddressEntity;
import java.sql.*;
import java.util.Optional;

public class AddressSQLDao implements IAddressDao {

    private final Connection connection;
    private final AddressIdentityMap identityMap;

    public AddressSQLDao(Connection connection) {
        this.connection = connection;
        this.identityMap = new AddressIdentityMap();
    }

    public AddressSQLDao(Connection connection, AddressIdentityMap identityMap) {
        this.connection = connection;
        this.identityMap = identityMap;
    }

    @Override
    public AddressEntity create(AddressEntity address) {
        String sql = "INSERT INTO address (street, city, house_number, postal_code) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getHouseNumber());
            ps.setString(4, address.getPostalCode());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    address.setId(rs.getLong(1));
                }
            }
            identityMap.put(address.getId(), address);
            return address;
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při ukládání adresy", e);
        }
    }

    @Override
    public Optional<AddressEntity> getById(Long id) {
        Optional<AddressEntity> cached = identityMap.get(id);
        if (cached.isPresent()) {
            return cached;
        }

        String sql = "SELECT id, street, city, house_number, postal_code FROM address WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AddressEntity address = new AddressEntity(
                            rs.getLong("id"),
                            rs.getString("street"),
                            rs.getString("city"),
                            rs.getString("house_number"),
                            rs.getString("postal_code")
                    );
                    identityMap.put(address.getId(), address);
                    return Optional.of(address);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při čtení adresy", e);
        }
    }
}