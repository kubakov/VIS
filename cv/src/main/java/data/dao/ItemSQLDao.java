package data.dao;

import data.model.ItemEntity;
import db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemSQLDao implements IItemDao {

    @Override
    public List<ItemEntity> getAllItems() {
        System.out.println("[ItemSQLDao] getAllItems() - načítání z H2 DB...");
        List<ItemEntity> items = new ArrayList<>();
        String sql = "SELECT id, name, description, price FROM items";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
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
            throw new RuntimeException("Chyba při čtení položek z H2 databáze", e);
        }
        return items;
    }

    @Override
    public void createItem(ItemEntity item) {
        System.out.println("[ItemSQLDao] createItem() - vkládání do H2 DB: " + item.getName());
        String sql = "INSERT INTO items (name, description, price) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
            throw new RuntimeException("Chyba při vkládání položky do H2 databáze", e);
        }
    }
}