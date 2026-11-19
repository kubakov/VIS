package db;

import uow.IUnitOfWorkFactory;
import uow.SQLUnitOfWorkFactory;

public class GlobalConn {
    private static IUnitOfWorkFactory uowFactory = new SQLUnitOfWorkFactory();

    public static void setFactory(IUnitOfWorkFactory factory) {
        uowFactory = factory;
    }

    public static IUnitOfWorkFactory getFactory() {
        return uowFactory;
    }
}