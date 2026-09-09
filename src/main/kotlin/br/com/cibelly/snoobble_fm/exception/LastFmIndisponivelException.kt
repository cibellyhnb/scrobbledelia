package br.com.cibelly.scrobbledelia.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class LastFmIndisponivelException(mensagem: String = "Last.fm está indisponível no momento") :
    RuntimeException(mensagem)