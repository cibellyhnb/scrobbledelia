package br.com.cibelly.scrobbledelia.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class LastFmNaoConectadoException : RuntimeException("Conecte sua conta do Last.fm antes de sincronizar")