package db;

import data.dao.IItemDao;
import data.dao.IOrderDao;
import data.dao.ItemTextDao;

public class TextConnection implements IDataConnection {

    @Override
    public IItemDao getItemDao() {
        return new ItemTextDao();
    }

    @Override
    public IOrderDao getOrderDao() {
        return null;
    }
}