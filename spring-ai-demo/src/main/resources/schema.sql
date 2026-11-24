CREATE TABLE IF NOT EXISTS chat_history (
    id         BIGSERIAL PRIMARY KEY,
    provider   VARCHAR(40)  NOT NULL,
    model      VARCHAR(100) NOT NULL,
    answer     TEXT,
    error      TEXT,
    latency_ms BIGINT       NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);
