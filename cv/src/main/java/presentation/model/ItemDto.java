package presentation.model;

public class ItemDto {
    private Long id;
    private String title;
    private String details;
    private String formattedPrice;

    public ItemDto(Long id, String title, String details, String formattedPrice) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.formattedPrice = formattedPrice;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDetails() { return details; }
    public String getFormattedPrice() { return formattedPrice; }

    @Override
    public String toString() {
        return "ItemDto [ID=" + id + ", Title='" + title + "', Details='" + details + "', Cena=" + formattedPrice + "]";
    }
}