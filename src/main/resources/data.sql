INSERT IGNORE INTO users (id, name, role) VALUES (1, 'Arshi', 'MAKER');
INSERT IGNORE INTO users (id, name, role) VALUES (2, 'Rahul', 'BENEFICIARY');
INSERT IGNORE INTO users (id, name, role) VALUES (3, 'Checker User', 'CHECKER');

INSERT IGNORE INTO accounts (id, account_number, account_type, currency, balance, available_balance, user_id) VALUES (1, '1234567890', 'CURRENT', 'INR', 100000, 100000, 1);

INSERT IGNORE INTO beneficiaries (id, name, account_number, bank_name, status) VALUES (1, 'Rahul', '9876543210', 'ABC Bank', 'ACTIVE');
