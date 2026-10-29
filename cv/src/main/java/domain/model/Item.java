package domain.model;

import java.math.BigDecimal;

public class Item {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    public Item() {
    }

    public Item(String title, String description, BigDecimal price) {
        this(null, title, description, price);
    }

    public Item(Long id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public boolean isValid(){
        return name != null && !name.trim().isEmpty() && price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}