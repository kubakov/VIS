package presentation.controller;

import domain.model.Item;
import domain.service.IItemService;
import domain.service.ItemService;
import presentation.mapper.ItemPresentationMapper;
import presentation.model.CreateItemDto;
import presentation.model.ItemDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemController {

    private final IItemService itemService;

    public ItemController() {
        this.itemService = new ItemService();
    }

    public List<ItemDto> getAllItems() {
        List<Item> domainItems = itemService.getAllItems();
        List<ItemDto> dtos = new ArrayList<>();

        for (Item item : domainItems) {
            dtos.add(ItemPresentationMapper.toDto(item));
        }
        return dtos;
    }

    public void createItem(CreateItemDto dto) {
        Item item = ItemPresentationMapper.toDomain(dto);
        itemService.createItem(item);
    }

    public void createItem(String title, String description, BigDecimal price) {
        createItem(new CreateItemDto(title, description, price));
    }
}