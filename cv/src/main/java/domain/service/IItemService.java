package domain.service;

import domain.model.Item;
import java.util.List;

public interface IItemService {
    List<Item> getAllItems();
    void createItem(Item item);
}