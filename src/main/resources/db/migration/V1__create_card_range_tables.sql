CREATE TABLE IF NOT EXISTS card_range_main
(
    id         BIGSERIAL PRIMARY KEY,
    bin        VARCHAR(10)  NOT NULL,
    min_range  NUMERIC       NOT NULL,
    max_range  NUMERIC       NOT NULL,
    alpha_code VARCHAR(10)  NOT NULL,
    bank_name  VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_min_range_main ON card_range_main (min_range);
CREATE INDEX IF NOT EXISTS idx_max_range_main ON card_range_main (max_range);

CREATE TABLE IF NOT EXISTS card_range_shadow
(
    id         BIGSERIAL PRIMARY KEY,
    bin        VARCHAR(10)  NOT NULL,
    min_range  NUMERIC       NOT NULL,
    max_range  NUMERIC       NOT NULL,
    alpha_code VARCHAR(2)   NOT NULL,
    bank_name  VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_min_range_shadow ON card_range_shadow (min_range);
CREATE INDEX IF NOT EXISTS idx_max_range_shadow ON card_range_shadow (max_range);
