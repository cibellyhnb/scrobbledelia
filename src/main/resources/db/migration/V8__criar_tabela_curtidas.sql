CREATE TABLE curtidas (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_curtida UNIQUE (usuario_id, post_id)
);

CREATE INDEX idx_curtidas_post ON curtidas(post_id);