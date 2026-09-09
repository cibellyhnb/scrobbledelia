CREATE TABLE follows (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seguidor_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    seguido_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_follow UNIQUE (seguidor_id, seguido_id),
    CONSTRAINT chk_nao_seguir_a_si_mesmo CHECK (seguidor_id != seguido_id)
);

CREATE INDEX idx_follows_seguidor ON follows(seguidor_id);
CREATE INDEX idx_follows_seguido ON follows(seguido_id);