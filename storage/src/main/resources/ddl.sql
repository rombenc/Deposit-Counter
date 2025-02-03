-- Customers table
CREATE TABLE customers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           contact VARCHAR(50) NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           UNIQUE KEY uq_customer_contact (contact)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Categories table
CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(50) NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            UNIQUE KEY uq_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Items table
CREATE TABLE items (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       description TEXT,
                       status ENUM('STORED', 'RETRIEVED', 'EXPIRED') NOT NULL DEFAULT 'STORED',
                       deposit_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       expected_pickup_date TIMESTAMP NOT NULL,
                       pickup_date TIMESTAMP NULL,
                       customer_id BIGINT NOT NULL,
                       category_id BIGINT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Indexes
                       INDEX idx_status (status),
                       INDEX idx_deposit_date (deposit_date),
                       INDEX idx_expected_pickup_date (expected_pickup_date),
                       INDEX idx_customer_id (customer_id),
                       INDEX idx_category_id (category_id),

    -- Foreign keys
                       CONSTRAINT fk_item_customer
                           FOREIGN KEY (customer_id)
                               REFERENCES customers(id)
                               ON DELETE RESTRICT
                               ON UPDATE CASCADE,

                       CONSTRAINT fk_item_category
                           FOREIGN KEY (category_id)
                               REFERENCES categories(id)
                               ON DELETE RESTRICT
                               ON UPDATE CASCADE,

    -- Check constraint for expected_pickup_date (MySQL 8.0.16+)
                       CONSTRAINT chk_expected_pickup_date
                           CHECK (expected_pickup_date > deposit_date),

    -- Check constraint for pickup_date (MySQL 8.0.16+)
                       CONSTRAINT chk_pickup_date
                           CHECK (pickup_date IS NULL OR pickup_date >= deposit_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create view for active items
CREATE OR REPLACE VIEW active_items AS
SELECT
    i.id,
    i.name AS item_name,
    i.description,
    i.status,
    i.deposit_date,
    i.expected_pickup_date,
    i.pickup_date,
    c.name AS customer_name,
    cat.name AS category_name,
    CASE
        WHEN i.expected_pickup_date < CURRENT_TIMESTAMP AND i.status = 'STORED'
            THEN 1
        ELSE 0
        END AS is_overdue
FROM
    items i
        JOIN customers c ON i.customer_id = c.id
        JOIN categories cat ON i.category_id = cat.id
WHERE
    i.status = 'STORED';

-- Add table comments (MySQL 8.0+)
ALTER TABLE items COMMENT 'Stores information about items in storage';
ALTER TABLE customers COMMENT 'Stores customer information';
ALTER TABLE categories COMMENT 'Stores item categories';