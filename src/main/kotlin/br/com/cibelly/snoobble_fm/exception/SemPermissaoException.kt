package br.com.cibelly.scrobbledelia.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class SemPermissaoException(mensagem: String = "Você não tem permissão para essa ação") : RuntimeException(mensagem)