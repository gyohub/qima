CREATE TABLE category (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  parent_id BIGINT,
  FOREIGN KEY (parent_id) REFERENCES category(id)
);

CREATE TABLE product (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  available BOOLEAN DEFAULT TRUE,
  category_id BIGINT,
  FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
);

-- Insert test categories
INSERT INTO category (id, name, parent_id) VALUES (1, 'Electronics', NULL);
INSERT INTO category (id, name, parent_id) VALUES (2, 'Laptops', 1);
INSERT INTO category (id, name, parent_id) VALUES (3, 'Phones', 1);

-- Insert test products
INSERT INTO product (name, description, price, available, category_id)
VALUES ('MacBook Pro', 'Apple laptop', 1999.99, true, 2),
       ('iPhone 14', 'Latest Apple phone', 999.99, true, 3);

-- Insert admin user (password is 'admin' bcrypt-encoded)
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$7sQY6j3CeRQla1xj8eEBtO.2mWmvQg6sd5gGEtn0Cds5fDk66H/4W', 'ADMIN');