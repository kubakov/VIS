package data.dao;

import data.model.ItemEntity;
import java.util.List;
import java.util.Optional;

public interface IItemDao {

    Optional<ItemEntity> getById(Long id);

    List<ItemEntity> getAllItems();

    void createItem(ItemEntity item);
}