package db;

import data.dao.IItemDao;
import data.dao.IOrderDao;
import data.dao.ItemSQLDao;
import data.dao.OrderSQLDao;

public class SQLConnection implements IDataConnection {

    @Override
    public IItemDao getItemDao() {
        return new ItemSQLDao();
    }

    @Override
    public IOrderDao getOrderDao() {
        return new OrderSQLDao();
    }
}