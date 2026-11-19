package uow;

import data.dao.IAddressDao;
import data.dao.ICustomerDao;
import data.dao.IItemDao;
import data.dao.IOrderDao;

public interface IUnitOfWork extends AutoCloseable {
    void commit();
    void rollback();
    @Override
    void close();

    IItemDao getItemDao();
    IOrderDao getOrderDao();
    ICustomerDao getCustomerDao();
    IAddressDao getAddressDao();
}