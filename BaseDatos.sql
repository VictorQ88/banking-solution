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