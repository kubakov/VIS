package db;

import data.dao.IItemDao;
import data.dao.IOrderDao;

public class GlobalConn {

    private static IDataConnection connection = new SQLConnection();

    public static void setConnection(IDataConnection newConnection) {
        connection = newConnection;
    }

    public static IDataConnection getConnection() {
        return connection;
    }

    public static IItemDao getItemDao() {
        return connection.getItemDao();
    }

    public static IOrderDao getOrderDao() { return connection.getOrderDao(); }
}