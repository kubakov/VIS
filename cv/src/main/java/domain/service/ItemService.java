package domain.service;

import data.mapper.ItemDataMapper;
import data.model.ItemEntity;
import db.GlobalConn;
import domain.model.Item;
import uow.IUnitOfWork;

import java.util.ArrayList;
import java.util.List;

public class ItemService implements IItemService {

    @Override
    public List<Item> getAllItems() {
        try (IUnitOfWork uow = GlobalConn.getFactory().createUnitOfWork()) {
            List<ItemEntity> entities = uow.getItemDao().getAllItems();
            List<Item> domainItems = new ArrayList<>();
            for (ItemEntity entity : entities) {
                domainItems.add(ItemDataMapper.toDomain(entity));
            }
            return domainItems;
        }
    }

    @Override
    public void createItem(Item item) {
        if (!item.isValid()) {
            throw new IllegalArgumentException("Neplatné údaje položky.");
        }
        try (IUnitOfWork uow = GlobalConn.getFactory().createUnitOfWork()) {
            uow.getItemDao().createItem(ItemDataMapper.toEntity(item));
            uow.commit();
        }
    }
}