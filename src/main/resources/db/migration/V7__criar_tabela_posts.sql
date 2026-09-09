CREATE TABLE posts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    scrobble_id UUID NOT NULL REFERENCES scrobbles(id) ON DELETE CASCADE,
    texto VARCHAR(500) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_posts_usuario ON posts(usuario_id);
CREATE INDEX idx_posts_criado_em ON posts(criado_em DESC);