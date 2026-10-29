package presentation.mapper;

import domain.model.Item;
import presentation.model.CreateItemDto;
import presentation.model.ItemDto;

import java.text.NumberFormat;
import java.util.Locale;

public class ItemPresentationMapper {

    private static final NumberFormat CURRENCY_FORMAT =
            NumberFormat.getCurrencyInstance(new Locale("cs", "CZ"));

    public static ItemDto toDto(Item item) {
        if (item == null) return null;

        String formattedPrice = (item.getPrice() != null)
                ? CURRENCY_FORMAT.format(item.getPrice())
                : "N/A";

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                formattedPrice
        );
    }

    public static Item toDomain(CreateItemDto dto) {
        if (dto == null) return null;
        return new Item(dto.getTitle(), dto.getDetails(), dto.getPrice());
    }
}