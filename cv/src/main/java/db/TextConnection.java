package db;

import data.dao.*;

public class TextConnection implements IDataConnection {

    @Override
    public IItemDao getItemDao() {
        return new ItemTextDao();
    }

    @Override
    public IOrderDao getOrderDao() {
        return null;
    }

    @Override
    public IAddressDao getAddressDao() {
        return null;
    }

    @Override
    public ICustomerDao getCustomerDao() {
        return null;
    }

}