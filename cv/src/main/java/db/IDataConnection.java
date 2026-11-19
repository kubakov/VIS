package db;

import data.dao.IAddressDao;
import data.dao.ICustomerDao;
import data.dao.IItemDao;
import data.dao.IOrderDao;

public interface IDataConnection {
    IItemDao getItemDao();
    IOrderDao getOrderDao();
    IAddressDao getAddressDao();
    ICustomerDao getCustomerDao();
}