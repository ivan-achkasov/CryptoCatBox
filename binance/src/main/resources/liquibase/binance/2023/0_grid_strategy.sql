--liquibase formatted sql

--changeset ivan.achkasov:base binance structure

CREATE SCHEMA binance;

CREATE DOMAIN currency_price AS numeric(20, 10);
CREATE DOMAIN currency_quantity AS numeric(20, 10);
CREATE DOMAIN currency_asset AS character varying(10);

CREATE TABLE binance.grid_strategy
(
    id                  BIGSERIAL PRIMARY KEY,
    start_price         currency_price    NOT NULL,
    base_asset          currency_asset    NOT NULL,
    quote_asset         currency_asset    NOT NULL,
    step_distinction    numeric(20, 10)   NOT NULL,
    sell_order_quantity currency_quantity NOT NULL,
    buy_order_quantity  currency_quantity NOT NULL,
    num_of_open_orders  smallint          NOT NULL,
    created_at          TIMESTAMP         NOT NULL DEFAULT now()
);

CREATE TABLE binance.futures_order
(
    id          BIGSERIAL PRIMARY KEY,
    exchange_id BIGINT                NOT NULL UNIQUE,
    base_asset  currency_asset        NOT NULL,
    quote_asset currency_asset        NOT NULL,
    price       currency_price        NOT NULL,
    quantity    currency_quantity     NOT NULL,
    side        character varying(20) NOT NULL,
    status      character varying(60) NOT NULL,
    created_at  TIMESTAMP             NOT NULL DEFAULT now()
);

CREATE TABLE binance.futures_order_to_strategy
(
    strategy_id BIGINT NOT NULL REFERENCES binance.grid_strategy (id) ON UPDATE CASCADE ON DELETE RESTRICT,
    order_id    BIGINT NOT NULL REFERENCES binance.futures_order (id) ON UPDATE CASCADE ON DELETE RESTRICT
);