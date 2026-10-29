package data.dao;

import data.model.ItemEntity;
import java.util.List;

public interface IItemDao {

    List<ItemEntity> getAllItems();

    void createItem(ItemEntity item);
}