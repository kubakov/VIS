package uow;

import data.dao.*;

public class TextUnitOfWork implements IUnitOfWork {

    private final IItemDao itemDao = new ItemTextDao();

    @Override public void commit() {}
    @Override public void rollback() {}
    @Override public void close() {}

    @Override public IItemDao getItemDao() { return itemDao; }
    @Override public IOrderDao getOrderDao() { throw new UnsupportedOperationException("Text storage nepodporuje objednávky."); }
    @Override public ICustomerDao getCustomerDao() { throw new UnsupportedOperationException("Text storage nepodporuje zákazníky."); }
    @Override public IAddressDao getAddressDao() { throw new UnsupportedOperationException("Text storage nepodporuje adresy."); }
}