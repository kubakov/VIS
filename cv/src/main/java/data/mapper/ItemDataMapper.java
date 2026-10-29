package data.mapper;

import data.model.ItemEntity;
import domain.model.Item;

public class ItemDataMapper {

    public static Item toDomain(ItemEntity entity) {
        if (entity == null) return null;
        return new Item(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice()
        );
    }

    public static ItemEntity toEntity(Item domain) {
        if (domain == null) return null;
        return new ItemEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getPrice()
        );
    }
}