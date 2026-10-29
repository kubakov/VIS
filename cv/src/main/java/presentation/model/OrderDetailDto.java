package presentation.model;

import java.util.List;

public class OrderDetailDto {
    private final Long orderId;
    private final String customerName;
    private final String customerContact;
    private final String address;
    private final String totalPrice;
    private final List<String> itemSummaries;

    public OrderDetailDto(Long orderId, String customerName, String customerContact,
                          String address, String totalPrice, List<String> itemSummaries) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.address = address;
        this.totalPrice = totalPrice;
        this.itemSummaries = itemSummaries;
    }

    public Long getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerContact() { return customerContact; }
    public String getAddress() { return address; }
    public String getTotalPrice() { return totalPrice; }
    public List<String> getItemSummaries() { return itemSummaries; }
}