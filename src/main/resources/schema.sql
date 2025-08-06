CREATE TABLE if not exists addresses
(
    id          int auto_increment PRIMARY KEY,
    street      VARCHAR(255),
    city        VARCHAR(100),
    state       VARCHAR(100),
    postal_code VARCHAR(20),
    country     VARCHAR(100)
);

CREATE TABLE if not exists customers
(
    id            int auto_increment PRIMARY KEY,
    name          VARCHAR(100)        NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    nid           VARCHAR(100) UNIQUE NOT NULL,
    phone         VARCHAR(25) UNIQUE  NOT NULL,
    date_of_birth timestamp           NOT NULL,
    address_id    INT REFERENCES addresses (id)
);

CREATE TABLE if not exists accounts
(
    id           int auto_increment PRIMARY KEY,
    customer_id  INT            NOT NULL REFERENCES customers (id),
    balance      DECIMAL(15, 2) NOT NULL DEFAULT 0,
    account_type VARCHAR(20),
    created_at   TIMESTAMP               DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE if not exists transactions
(
    id               int auto_increment PRIMARY KEY,
    from_account     INT,
    to_account       INT,
    amount           DECIMAL(15, 2) NOT NULL,
    transaction_type VARCHAR(20),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
