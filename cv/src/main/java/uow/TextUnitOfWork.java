package uow;

import data.dao.*;
import data.identitymap.ItemIdentityMap;

public class TextUnitOfWork implements IUnitOfWork {

    private final ItemIdentityMap itemIdentityMap = new ItemIdentityMap();
    private final IItemDao itemDao = new ItemTextDao(itemIdentityMap);

    @Override public void commit() {}
    @Override public void rollback() {}

    @Override
    public void close() {
        itemIdentityMap.clear();
    }

    @Override public IItemDao getItemDao() { return itemDao; }
    @Override public IOrderDao getOrderDao() { throw new UnsupportedOperationException("Text storage nepodporuje objednávky."); }
    @Override public ICustomerDao getCustomerDao() { throw new UnsupportedOperationException("Text storage nepodporuje zákazníky."); }
    @Override public IAddressDao getAddressDao() { throw new UnsupportedOperationException("Text storage nepodporuje adresy."); }
}