package data.dao;

import data.identitymap.ItemIdentityMap;
import data.model.ItemEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemSQLDao implements IItemDao {

    private final Connection connection;
    private final ItemIdentityMap identityMap;

    public ItemSQLDao(Connection connection) {
        this.connection = connection;
        this.identityMap = new ItemIdentityMap();
    }

    public ItemSQLDao(Connection connection, ItemIdentityMap identityMap) {
        this.connection = connection;
        this.identityMap = identityMap;
    }

    @Override
    public Optional<ItemEntity> getById(Long id) {
        Optional<ItemEntity> cached = identityMap.get(id);
        if (cached.isPresent()) {
            return cached;
        }

        String sql = "SELECT id, name, description, price FROM items WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    ItemEntity item = new ItemEntity(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBigDecimal("price")
                    );
                    identityMap.put(item.getId(), item);
                    return Optional.of(item);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při čtení položky", e);
        }
    }

    @Override
    public List<ItemEntity> getAllItems() {
        List<ItemEntity> items = new ArrayList<>();
        String sql = "SELECT id, name, description, price FROM items";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                ItemEntity item = identityMap.get(id).orElse(null);
                if(item == null){
                    item = new ItemEntity(
                        id,
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price")
                    );
                    identityMap.put(id, item);
                }
                items.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při čtení položek z DB", e);
        }
        return items;
    }

    @Override
    public void createItem(ItemEntity item) {
        String sql = "INSERT INTO items (name, description, price) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setBigDecimal(3, item.getPrice());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                }
            }
            identityMap.put(item.getId(), item);
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při vkládání položky do DB", e);
        }
    }
}