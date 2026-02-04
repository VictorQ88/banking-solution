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