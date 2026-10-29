package domain.service;

import data.dao.IItemDao;
import data.mapper.ItemDataMapper;
import data.model.ItemEntity;
import db.GlobalConn;
import domain.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemService implements IItemService {

    @Override
    public List<Item> getAllItems() {
        IItemDao dao = GlobalConn.getItemDao();
        List<ItemEntity> entities = dao.getAllItems();

        List<Item> domainItems = new ArrayList<>();
        for (ItemEntity entity : entities) {
            domainItems.add(ItemDataMapper.toDomain(entity));
        }
        return domainItems;
    }

    @Override
    public void createItem(Item item) {
        if (!item.isValid()) {
            throw new IllegalArgumentException("Neplatné údaje položky.");
        }
        IItemDao dao = GlobalConn.getItemDao();
        dao.createItem(ItemDataMapper.toEntity(item));
    }
}