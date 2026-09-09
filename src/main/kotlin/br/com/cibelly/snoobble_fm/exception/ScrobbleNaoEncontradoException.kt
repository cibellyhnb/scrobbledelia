package br.com.cibelly.scrobbledelia.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@ResponseStatus(HttpStatus.NOT_FOUND)
class ScrobbleNaoEncontradoException(id: UUID) : RuntimeException("Scrobble não encontrado: $id")