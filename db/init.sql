-- 创建数据库
CREATE DATABASE IF NOT EXISTS warehouse DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE warehouse;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `real_name` VARCHAR(50) DEFAULT NULL,
    `role` VARCHAR(10) NOT NULL DEFAULT 'user' COMMENT 'admin / user',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品表
CREATE TABLE IF NOT EXISTS `product` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `product_code` VARCHAR(50) NOT NULL UNIQUE,
    `product_name` VARCHAR(100) NOT NULL,
    `unit` VARCHAR(20) DEFAULT '个',
    `stock_quantity` INT DEFAULT 0,
    `location` VARCHAR(100) DEFAULT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 入库记录表
CREATE TABLE IF NOT EXISTS `stock_in` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL,
    `operator_id` INT DEFAULT NULL,
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT 'pending / confirmed',
    `operate_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `remark` VARCHAR(255) DEFAULT NULL,
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 出库记录表
CREATE TABLE IF NOT EXISTS `stock_out` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL,
    `operator_id` INT DEFAULT NULL,
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT 'pending / confirmed',
    `operate_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `remark` VARCHAR(255) DEFAULT NULL,
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始化管理员和测试用户 (密码明文存储，生产环境应加密)
INSERT INTO `user` (`username`, `password`, `real_name`, `role`) VALUES
('admin', 'admin123', '系统管理员', 'admin'),
('user1', 'user123', '测试用户', 'user');

-- 初始化测试产品
INSERT INTO `product` (`product_code`, `product_name`, `unit`, `stock_quantity`, `location`) VALUES
('P001', '笔记本电脑', '台', 50, 'A区-01货架'),
('P002', '机械键盘', '个', 200, 'A区-02货架'),
('P003', '显示器', '台', 30, 'B区-01货架'),
('P004', '鼠标垫', '个', 500, 'B区-02货架');
