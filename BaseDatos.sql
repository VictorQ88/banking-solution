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

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE clients
TO banking_user;

GRANT USAGE, SELECT
ON SEQUENCE clients_id_seq
TO banking_user;

-- Account

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

GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLE accounts
TO banking_user;

GRANT USAGE, SELECT
ON SEQUENCE accounts_id_seq
TO banking_user;