ALTER TABLE notificacoes
ADD COLUMN referencia_externa VARCHAR(200);

CREATE INDEX idx_notificacoes_referencia ON notificacoes(usuario_id, tipo, referencia_externa);