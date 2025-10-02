DROP DATABASE IF EXISTS ltw_bt10;
CREATE DATABASE ltw_bt10 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ltw_bt10;

CREATE TABLE roles (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       fullname VARCHAR(60) NOT NULL,
                       email VARCHAR(120) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(20)
);

CREATE TABLE categories (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(120) NOT NULL UNIQUE,
                            images VARCHAR(255)
);

CREATE TABLE user_categories (
                                 user_id BIGINT NOT NULL,
                                 category_id BIGINT NOT NULL,
                                 PRIMARY KEY(user_id, category_id),
                                 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                 FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY(user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE products (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          title VARCHAR(200) NOT NULL,
                          quantity INT DEFAULT 0,
                          description TEXT,
                          price DOUBLE NOT NULL,
                          user_id BIGINT,
                          category_id BIGINT,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
                          FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);