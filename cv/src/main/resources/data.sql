INSERT INTO customer (fname, lname, phone, email)
VALUES ('Jan', 'Novák', '+420 777 123 456', 'jan.novak@email.cz');

INSERT INTO address (street, city, house_number, postal_code)
VALUES ('Hlavní', 'Praha', '42/A', '110 00');

INSERT INTO items (name, description, price) VALUES
('Mechanická klávesnice', 'Herní RGB klávesnice', 2499.00),
('Optická myš', 'Bezdrátová ergonomická myš', 1199.00),
('Podložka pod myš', 'Velká textilní podložka', 399.00);

INSERT INTO "order" (customer_id, address_id) VALUES (1, 1);

INSERT INTO shopping_cart (order_id, item_id, curr_price) VALUES
(1, 1, 2499.00),
(1, 2, 1199.00);