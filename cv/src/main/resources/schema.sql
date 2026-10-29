DROP TABLE IF EXISTS shopping_cart;
DROP TABLE IF EXISTS "order";
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS address;

CREATE TABLE customer (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          fname VARCHAR(100) NOT NULL,
                          lname VARCHAR(100) NOT NULL,
                          phone VARCHAR(30),
                          email VARCHAR(100) NOT NULL
);

CREATE TABLE address (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         street VARCHAR(150) NOT NULL,
                         city VARCHAR(100) NOT NULL,
                         house_number VARCHAR(20) NOT NULL,
                         postal_code VARCHAR(20) NOT NULL
);

CREATE TABLE items (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(150) NOT NULL,
                       description VARCHAR(255),
                       price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE "order" (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         customer_id BIGINT NOT NULL,
                         address_id BIGINT NOT NULL,
                         CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
                         CONSTRAINT fk_order_address FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE shopping_cart (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               order_id BIGINT NOT NULL,
                               item_id BIGINT NOT NULL,
                               curr_price DECIMAL(10, 2) NOT NULL,
                               CONSTRAINT fk_cart_order FOREIGN KEY (order_id) REFERENCES "order"(id) ON DELETE CASCADE,
                               CONSTRAINT fk_cart_item FOREIGN KEY (item_id) REFERENCES items(id)
);