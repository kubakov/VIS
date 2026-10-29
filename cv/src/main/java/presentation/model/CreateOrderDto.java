package presentation.model;

import domain.model.Item;
import java.util.List;

public class CreateOrderDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private String street;
    private String city;
    private String houseNumber;
    private String postalCode;

    private List<Item> cartItems;

    public CreateOrderDto(String firstName, String lastName, String email, String phone,
                          String street, String city, String houseNumber, String postalCode,
                          List<Item> cartItems) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.street = street;
        this.city = city;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.cartItems = cartItems;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public List<Item> getCartItems() {
        return cartItems;
    }
}
