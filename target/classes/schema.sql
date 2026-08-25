CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    account_type VARCHAR(50) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    balance BIGINT NOT NULL,
    available_balance BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS beneficiaries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    bank_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id VARCHAR(100) NOT NULL UNIQUE,
    debit_account_id BIGINT NOT NULL,
    beneficiary_id BIGINT NOT NULL,
    amount BIGINT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_by BIGINT NOT NULL,
    approved_by BIGINT,
    rejection_reason VARCHAR(500),
    created_at TIMESTAMP,
    approved_at TIMESTAMP
);
