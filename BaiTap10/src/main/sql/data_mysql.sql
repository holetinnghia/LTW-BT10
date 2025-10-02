USE ltw_bt10;
INSERT INTO roles(name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

-- mật khẩu: 123456 đã mã hoá BCrypt mẫu, thay bằng app seeding nếu khác encoder
-- hash sau chỉ minh hoạ, nên tạo bằng app khi chạy lần đầu
INSERT INTO users(fullname,email,password,phone) VALUES
                                                     ('Admin','admin@example.com','$2a$10$F2rOe0dG1rQk2n0nWtWbUu8mGxqB2X6dQnOQ3aQeO7m2b0pA8wq8S','+84900000001'),
                                                     ('User A','usera@example.com','$2a$10$F2rOe0dG1rQk2n0nWtWbUu8mGxqB2X6dQnOQ3aQeO7m2b0pA8wq8S','+84900000002');

INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON u.email='admin@example.com' AND r.name='ROLE_ADMIN';
INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON u.email='usera@example.com' AND r.name='ROLE_USER';

INSERT INTO categories(name,images) VALUES
                                        ('Hoa Hồng','rose.jpg'),('Hoa Ly','lily.jpg'),('Hoa Cúc','chrys.jpg');

INSERT INTO user_categories(user_id, category_id)
SELECT (SELECT id FROM users WHERE email='usera@example.com'), c.id FROM categories c;

INSERT INTO products(title,quantity,description,price,user_id,category_id) VALUES
                                                                               ('Bó Hồng Đỏ',10,'Hoa hồng đỏ cao cấp',150000,(SELECT id FROM users WHERE email='usera@example.com'), (SELECT id FROM categories WHERE name='Hoa Hồng')),
                                                                               ('Bó Ly Trắng',5,'Hoa ly trắng thơm',180000,(SELECT id FROM users WHERE email='usera@example.com'), (SELECT id FROM categories WHERE name='Hoa Ly')),
                                                                               ('Giỏ Cúc Vàng',8,'Cúc vàng tươi',90000,(SELECT id FROM users WHERE email='usera@example.com'), (SELECT id FROM categories WHERE name='Hoa Cúc'));