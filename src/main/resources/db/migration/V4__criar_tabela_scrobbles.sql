CREATE TABLE scrobbles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    musica VARCHAR(300) NOT NULL,
    artista VARCHAR(300) NOT NULL,
    album VARCHAR(300),
    tocado_em TIMESTAMP NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_scrobble UNIQUE (usuario_id, artista, musica, tocado_em)
);

CREATE INDEX idx_scrobbles_usuario ON scrobbles(usuario_id);
CREATE INDEX idx_scrobbles_tocado_em ON scrobbles(tocado_em);