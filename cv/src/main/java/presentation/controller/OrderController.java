package presentation.controller;

import domain.model.Order;
import domain.service.IOrderService;
import domain.service.OrderService;
import presentation.mapper.OrderPresentationMapper;
import presentation.model.CreateOrderDto;
import presentation.model.OrderDetailDto;

import java.util.Optional;

public class OrderController {
    private final IOrderService orderService = new OrderService();

    public Optional<OrderDetailDto> getOrderDetails(Long id) {
        return orderService.getOrderById(id)
                .map(OrderPresentationMapper::toDto);
    }

    public OrderDetailDto createOrder(CreateOrderDto dto) {
        Order domainOrder = OrderPresentationMapper.toDomain(dto);
        Order createdOrder = orderService.createOrder(domainOrder);

        OrderDetailDto detailDto = OrderPresentationMapper.toDto(createdOrder);
        printOrderToConsole(detailDto);

        return detailDto;
    }

    private void printOrderToConsole(OrderDetailDto dto) {
        System.out.println("\n========================================");
        System.out.println("NOVÁ OBJEDNÁVKA VYTVOŘENA:");
        System.out.println("Objednávka č.: " + dto.getOrderId());
        System.out.println("Zákazník:      " + dto.getCustomerName() + " (" + dto.getCustomerContact() + ")");
        System.out.println("Adresa:        " + dto.getAddress());
        System.out.println("Položky:");
        dto.getItemSummaries().forEach(i -> System.out.println("  • " + i));
        System.out.println("Cena celkem:   " + dto.getTotalPrice());
        System.out.println("========================================\n");
    }
}