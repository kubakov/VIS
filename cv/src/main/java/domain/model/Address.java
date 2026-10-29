package domain.model;

public class Address {
    private Long id;
    private String street;
    private String city;
    private String houseNumber;
    private String postalCode;

    public Address(Long id, String street, String city, String houseNumber, String postalCode) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
    }

    public String getFormattedAddress() {
        return street + " " + houseNumber + ", " + postalCode + " " + city;
    }

    public Long getId() { return id; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getHouseNumber() { return houseNumber; }
    public String getPostalCode() { return postalCode; }
}