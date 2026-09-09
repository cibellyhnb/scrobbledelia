package br.com.cibelly.scrobbledelia.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
class LastFmRateLimitException :
    RuntimeException("Limite de requisições ao Last.fm excedido. Tente novamente em instantes.")