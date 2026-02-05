-- Client
CREATE TABLE clients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    age INT NOT NULL,
    identification VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    client_id VARCHAR(40) NOT NULL,
    password VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL,

    CONSTRAINT uk_clients_identification UNIQUE (identification),
    CONSTRAINT uk_clients_client_id UNIQUE (client_id)
);

CREATE SEQUENCE clientid_seq START WITH 1 INCREMENT BY 1;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE clients
TO banking_user;

GRANT USAGE, SELECT
ON SEQUENCE clients_id_seq
TO banking_user;

GRANT USAGE, SELECT
ON SEQUENCE clientid_seq
TO banking_user;


-- Account
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    initial_balance NUMERIC(12,2) NOT NULL,
    active BOOLEAN NOT NULL,
    client_id BIGINT NOT NULL,

    CONSTRAINT uk_accounts_account_number UNIQUE (account_number),
    CONSTRAINT fk_accounts_client FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- sequence for numbering accounts
CREATE SEQUENCE account_number_seq START WITH 1000000000 INCREMENT BY 1;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE accounts
TO banking_user;

GRANT USAGE, SELECT
ON SEQUENCE accounts_id_seq
TO banking_user;

GRANT USAGE, SELECT
ON SEQUENCE account_number_seq
TO banking_user;


-- Movements
CREATE TABLE movements (
    id BIGSERIAL PRIMARY KEY,
    movement_date TIMESTAMP NOT NULL,
    movement_type VARCHAR(20) NOT NULL,
    value NUMERIC(12,2) NOT NULL,
    balance NUMERIC(12,2) NOT NULL,
    account_id BIGINT NOT NULL
);

ALTER TABLE movements
ADD CONSTRAINT fk_movements_account
FOREIGN KEY (account_id)
REFERENCES accounts(id)
ON DELETE CASCADE;

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE movements
TO banking_user;

GRANT USAGE, SELECT
ON SEQUENCE movements_id_seq
TO banking_user;

CREATE INDEX idx_movements_account_id ON movements(account_id);


-- =========================
-- INITIAL DATA
-- =========================

-- Clients
INSERT INTO clients (name, gender, age, identification, address, phone, client_id, password, active)
VALUES
('Jose Lema', 'M', 30, '0102030406', 'Otavalo sn y principal', '098254785', 'CLI-000001', 'MTIzNA==', TRUE),
('Marianela Montalvo', 'F', 28, '0102030407', 'Amazonas y NNUU',        '097548965', 'CLI-000002', 'NTY3OA==', TRUE),
('Juan Osorio', 'M', 35, '0102030408', '13 junio y Equinoccial',       '098874587', 'CLI-000003', 'MTI0NQ==', TRUE);

SELECT setval('clientid_seq', 3, true);

-- Accounts
-- Jose Lema -> 478758 Ahorro 2000 True
-- Marianela -> 225487 Corriente 100 True
-- Juan Osorio -> 495878 Ahorros 0 True
-- Marianela -> 496825 Ahorros 540 True
-- Jose Lema -> 585545 Corriente 1000 True

INSERT INTO accounts (account_number, account_type, initial_balance, active, client_id)
VALUES
('478758', 'SAVINGS',  2000.00, TRUE,  (SELECT id FROM clients WHERE identification='0102030406')),
('225487', 'CHECKING',  100.00, TRUE,  (SELECT id FROM clients WHERE identification='0102030407')),
('495878', 'SAVINGS',     0.00, TRUE,  (SELECT id FROM clients WHERE identification='0102030408')),
('496825', 'SAVINGS',   540.00, TRUE,  (SELECT id FROM clients WHERE identification='0102030407')),
('585545', 'CHECKING', 1000.00, TRUE,  (SELECT id FROM clients WHERE identification='0102030406'));

SELECT setval('account_number_seq', 585545, true);

-- Movements:

INSERT INTO movements (movement_date, movement_type, value, balance, account_id)
SELECT NOW(), 'DEPOSIT', a.initial_balance, a.initial_balance, a.id
FROM accounts a
WHERE a.account_number IN ('478758','225487','495878','496825','585545');

INSERT INTO movements (movement_date, movement_type, value, balance, account_id)
SELECT NOW(), 'WITHDRAWAL', -575.00,
       (SELECT m.balance FROM movements m WHERE m.account_id=a.id ORDER BY m.id DESC LIMIT 1) + (-575.00),
       a.id
FROM accounts a WHERE a.account_number='478758';

INSERT INTO movements (movement_date, movement_type, value, balance, account_id)
SELECT NOW(), 'DEPOSIT', 600.00,
       (SELECT m.balance FROM movements m WHERE m.account_id=a.id ORDER BY m.id DESC LIMIT 1) + (600.00),
       a.id
FROM accounts a WHERE a.account_number='225487';

INSERT INTO movements (movement_date, movement_type, value, balance, account_id)
SELECT NOW(), 'DEPOSIT', 150.00,
       (SELECT m.balance FROM movements m WHERE m.account_id=a.id ORDER BY m.id DESC LIMIT 1) + (150.00),
       a.id
FROM accounts a WHERE a.account_number='495878';

INSERT INTO movements (movement_date, movement_type, value, balance, account_id)
SELECT NOW(), 'WITHDRAWAL', -540.00,
       (SELECT m.balance FROM movements m WHERE m.account_id=a.id ORDER BY m.id DESC LIMIT 1) + (-540.00),
       a.id
FROM accounts a WHERE a.account_number='496825';