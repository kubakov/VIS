package db;

import data.dao.IItemDao;
import data.dao.IOrderDao;

public interface IDataConnection {
    IItemDao getItemDao();
    IOrderDao getOrderDao();
}