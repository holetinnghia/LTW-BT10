-- 0) Tạo database
IF DB_ID('bt10') IS NULL
BEGIN
  CREATE DATABASE bt10;
END
GO
USE bt10;
GO

-- 1) Bảng roles, users, user_roles, categories (nếu JPA chưa tự tạo)
IF OBJECT_ID('roles','U') IS NULL
CREATE TABLE roles (
                       id INT IDENTITY PRIMARY KEY,
                       name NVARCHAR(50) UNIQUE NOT NULL
);
IF OBJECT_ID('users','U') IS NULL
CREATE TABLE users (
                       id INT IDENTITY PRIMARY KEY,
                       username NVARCHAR(50) UNIQUE NOT NULL,
                       password NVARCHAR(200) NOT NULL,
                       enabled BIT NOT NULL DEFAULT 1
);
IF OBJECT_ID('user_roles','U') IS NULL
CREATE TABLE user_roles (
                            user_id INT NOT NULL FOREIGN KEY REFERENCES users(id) ON DELETE CASCADE,
                            role_id INT NOT NULL FOREIGN KEY REFERENCES roles(id) ON DELETE CASCADE,
                            CONSTRAINT PK_user_roles PRIMARY KEY (user_id, role_id)
);
IF OBJECT_ID('categories','U') IS NULL
CREATE TABLE categories (
                            id INT IDENTITY PRIMARY KEY,
                            name NVARCHAR(100) UNIQUE NOT NULL,
                            description NVARCHAR(255) NULL,
                            active BIT NOT NULL DEFAULT 1,
                            createdAt DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

-- 2) Seed roles
IF NOT EXISTS (SELECT 1 FROM roles WHERE name='ROLE_USER') INSERT INTO roles(name) VALUES('ROLE_USER');
IF NOT EXISTS (SELECT 1 FROM roles WHERE name='ROLE_ADMIN') INSERT INTO roles(name) VALUES('ROLE_ADMIN');

-- 3) Seed users (password dạng {noop}*** để demo)
IF NOT EXISTS (SELECT 1 FROM users WHERE username='admin')
INSERT INTO users(username,password,enabled) VALUES('admin','{noop}admin123',1);
IF NOT EXISTS (SELECT 1 FROM users WHERE username='user')
INSERT INTO users(username,password,enabled) VALUES('user','{noop}user123',1);

-- 4) Map roles
DECLARE @adminId INT = (SELECT id FROM users WHERE username='admin');
DECLARE @userId  INT = (SELECT id FROM users WHERE username='user');
DECLARE @rUser  INT = (SELECT id FROM roles WHERE name='ROLE_USER');
DECLARE @rAdmin INT = (SELECT id FROM roles WHERE name='ROLE_ADMIN');

IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id=@adminId AND role_id=@rAdmin)
  INSERT INTO user_roles(user_id,role_id) VALUES(@adminId,@rAdmin);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id=@adminId AND role_id=@rUser)
  INSERT INTO user_roles(user_id,role_id) VALUES(@adminId,@rUser);
IF NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id=@userId AND role_id=@rUser)
  INSERT INTO user_roles(user_id,role_id) VALUES(@userId,@rUser);

-- 5) Seed categories
IF NOT EXISTS (SELECT 1 FROM categories)
BEGIN
INSERT INTO categories(name,description,active) VALUES
                                                    (N'Điện thoại',N'Danh mục điện thoại',1),
                                                    (N'Laptop',N'Danh mục laptop',1),
                                                    (N'Tablet',N'Danh mục tablet',1),
                                                    (N'Phụ kiện',N'Phụ kiện công nghệ',1),
                                                    (N'Thiết bị mạng',N'Router, Switch,...',1),
                                                    (N'Màn hình',N'Màn hình máy tính',1),
                                                    (N'Âm thanh',N'Tai nghe, loa',1),
                                                    (N'Smart Home',N'Thiết bị nhà thông minh',1),
                                                    (N'Gaming',N'Thiết bị chơi game',1),
                                                    (N'Lưu trữ',N'SSD, HDD, thẻ nhớ',1),
                                                    (N'Camera',N'Camera, webcam',1),
                                                    (N'Khác',N'Khác',1);
END
GO

-- 6) Kiểm tra nhanh
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS total_roles FROM roles;
SELECT COUNT(*) AS total_links FROM user_roles;
SELECT COUNT(*) AS total_categories FROM categories;