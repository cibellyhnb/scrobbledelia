package br.com.cibelly.scrobbledelia.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class LastFmUsuarioInvalidoException(username: String) :
    RuntimeException("Usuário '$username' não encontrado no Last.fm")