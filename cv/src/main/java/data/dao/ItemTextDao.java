package data.dao;

import data.model.ItemEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemTextDao implements IItemDao {

    private static final List<ItemEntity> FILE_STORAGE = new ArrayList<>();
    private static long nextId = 10L;

    static {
        FILE_STORAGE.add(new ItemEntity(nextId++, "TXT Klávesnice", "Mechanická klávesnice", new BigDecimal("1500.00")));
    }

    @Override
    public List<ItemEntity> getAllItems() {
        System.out.println("[ItemTextDao] getAllItems() - čtení z textového souboru...");
        return new ArrayList<>(FILE_STORAGE);
    }

    @Override
    public void createItem(ItemEntity item) {
        System.out.println("[ItemTextDao] createItem() - zápis do textového souboru: " + item.getName());
        if(item.getId() == null) {
            item.setId(nextId++);
        }
        FILE_STORAGE.add(item);
    }
}