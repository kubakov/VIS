package presentation.model;

import java.math.BigDecimal;

public class CreateItemDto {
    private String title;
    private String details;
    private BigDecimal price;

    public CreateItemDto() {}

    public CreateItemDto(String title, String details, BigDecimal price) {
        this.title = title;
        this.details = details;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
