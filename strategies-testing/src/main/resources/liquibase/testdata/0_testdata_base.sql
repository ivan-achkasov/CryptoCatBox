--liquibase formatted sql

--changeset ivan.achkasov:base candles structure

CREATE SCHEMA candles;

CREATE TABLE candles.binance_btc_usdt_spot_1m_candles
(
    start_time  TIMESTAMP PRIMARY KEY,
    open_price  NUMERIC(7, 8)   NOT NULL,
    close_price NUMERIC(7, 8)   NOT NULL,
    high_price  NUMERIC(7, 8)   NOT NULL,
    low_price   NUMERIC(7, 8)   NOT NULL,
    volume      NUMERIC(20, 10) NOT NULL
);
