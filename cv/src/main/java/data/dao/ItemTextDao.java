package data.dao;

import data.identitymap.ItemIdentityMap;
import data.model.ItemEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemTextDao implements IItemDao {

    private static final List<ItemEntity> FILE_STORAGE = new ArrayList<>();
    private static long nextId = 10L;

    static {
        FILE_STORAGE.add(new ItemEntity(nextId++, "TXT Klávesnice", "Mechanická klávesnice", new BigDecimal("1500.00")));
    }

    private final ItemIdentityMap identityMap;

    public ItemTextDao() {
        this.identityMap = new ItemIdentityMap();
    }

    public ItemTextDao(ItemIdentityMap identityMap) {
        this.identityMap = identityMap;
    }

    @Override
    public Optional<ItemEntity> getById(Long id) {
        Optional<ItemEntity> cached = identityMap.get(id);
        if (cached.isPresent()) {
            return cached;
        }

        return FILE_STORAGE.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .map(item -> {
                    identityMap.put(item.getId(), item);
                    return item;
                });
    }

    @Override
    public List<ItemEntity> getAllItems() {
        System.out.println("[ItemTextDao] getAllItems() - čtení z textového souboru...");
        for(ItemEntity item : FILE_STORAGE) {
            identityMap.put(item.getId(), item);
        }
        return new ArrayList<>(FILE_STORAGE);
    }

    @Override
    public void createItem(ItemEntity item) {
        System.out.println("[ItemTextDao] createItem() - zápis do textového souboru: " + item.getName());
        if(item.getId() == null) {
            item.setId(nextId++);
        }
        FILE_STORAGE.add(item);
        identityMap.put(item.getId(), item);
    }
}