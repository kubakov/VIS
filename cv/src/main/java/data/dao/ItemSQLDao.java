package data.dao;

import data.model.ItemEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemSQLDao implements IItemDao {

    private final Connection connection;

    public ItemSQLDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<ItemEntity> getAllItems() {
        List<ItemEntity> items = new ArrayList<>();
        String sql = "SELECT id, name, description, price FROM items";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new ItemEntity(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price")
                ));
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
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při vkládání položky do DB", e);
        }
    }
}