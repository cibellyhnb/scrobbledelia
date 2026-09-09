package br.com.cibelly.scrobbledelia.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class SeguirASiMesmoException : RuntimeException("Você não pode seguir a si mesmo")