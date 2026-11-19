package db;

import data.dao.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnection implements IDataConnection, AutoCloseable {

    private Connection connection;

    public SQLConnection() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při vytváření SQL spojení", e);
        }
    }

    public SQLConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public IItemDao getItemDao() {
        return new ItemSQLDao(this.connection);
    }

    @Override
    public IOrderDao getOrderDao(){
        return new OrderSQLDao(this.connection);
    }

    @Override
    public IAddressDao getAddressDao(){
        return new AddressSQLDao(this.connection);
    }

    @Override
    public ICustomerDao getCustomerDao(){
        return new CustomerSQLDao(this.connection);
    }

    @Override
    public void close() throws Exception {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Chyba při zavírání spojení: " + e.getMessage());
        }
    }
}