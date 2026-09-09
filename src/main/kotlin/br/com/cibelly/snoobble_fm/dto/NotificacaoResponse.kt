package br.com.cibelly.scrobbledelia.dto

import java.time.LocalDateTime
import java.util.UUID

data class NotificacaoResponse(
    val id: UUID,
    val tipo: String,
    val titulo: String,
    val mensagem: String,
    val lida: Boolean,
    val criadoEm: LocalDateTime
)