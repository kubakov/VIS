package uow;

import data.dao.*;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLUnitOfWork implements IUnitOfWork {

    private final Connection connection;
    private boolean committed = false;

    private IItemDao itemDao;
    private IOrderDao orderDao;
    private ICustomerDao customerDao;
    private IAddressDao addressDao;

    public SQLUnitOfWork(Connection connection) {
        this.connection = connection;
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Nepodařilo se nastavit transakci (autoCommit=false)", e);
        }
    }

    @Override
    public IItemDao getItemDao() {
        if (itemDao == null) itemDao = new ItemSQLDao(connection);
        return itemDao;
    }

    @Override
    public IOrderDao getOrderDao() {
        if (orderDao == null) orderDao = new OrderSQLDao(connection);
        return orderDao;
    }

    @Override
    public ICustomerDao getCustomerDao() {
        if (customerDao == null) customerDao = new CustomerSQLDao(connection);
        return customerDao;
    }

    @Override
    public IAddressDao getAddressDao() {
        if (addressDao == null) addressDao = new AddressSQLDao(connection);
        return addressDao;
    }

    @Override
    public void commit() {
        try {
            connection.commit();
            committed = true;
        } catch (SQLException e) {
            throw new RuntimeException("Chyba při commitování transakce", e);
        }
    }

    @Override
    public void rollback() {
        try {
            if (!connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Chyba při rollbacku: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (!committed) {
                rollback();
            }
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            System.err.println("Chyba při zavírání UnitOfWork: " + e.getMessage());
        }
    }
}