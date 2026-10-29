package presentation.mapper;

import domain.model.Address;
import domain.model.Customer;
import domain.model.Order;
import domain.model.OrderItem;
import presentation.model.CreateOrderDto;
import presentation.model.OrderDetailDto;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class OrderPresentationMapper {

    private static final NumberFormat CURRENCY_FORMAT =
            NumberFormat.getCurrencyInstance(new Locale("cs", "CZ"));

    public static OrderDetailDto toDto(Order domain) {
        if (domain == null) return null;

        return new OrderDetailDto(
                domain.getId(),
                domain.getCustomer().getFullName(),
                domain.getCustomer().getEmail() + " | " + domain.getCustomer().getPhone(),
                domain.getDeliveryAddress().getFormattedAddress(),
                CURRENCY_FORMAT.format(domain.calculateTotalPrice()),
                domain.getItems().stream()
                        .map(item -> item.getItem().getName() + " (" + CURRENCY_FORMAT.format(item.getPriceAtPurchase()) + ")")
                        .collect(Collectors.toList())
        );
    }

    public static Order toDomain(CreateOrderDto dto) {
        if (dto == null) return null;

        Customer customer = new Customer(
                null,
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPhone(),
                dto.getEmail()
        );

        Address address = new Address(
                null,
                dto.getStreet(),
                dto.getCity(),
                dto.getHouseNumber(),
                dto.getPostalCode()
        );

        List<OrderItem> orderItems = dto.getCartItems().stream()
                .map(item -> new OrderItem(item, item.getPrice()))
                .collect(Collectors.toList());

        return new Order(customer, address, orderItems);
    }
}