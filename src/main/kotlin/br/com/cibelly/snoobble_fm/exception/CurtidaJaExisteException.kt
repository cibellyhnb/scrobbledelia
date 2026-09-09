package br.com.cibelly.scrobbledelia.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class CurtidaJaExisteException : RuntimeException("Você já curtiu esse post")