package br.com.cibelly.scrobbledelia.dto

import java.time.LocalDateTime
import java.util.UUID

data class ComentarioResponse(
    val id: UUID,
    val usuarioId: UUID,
    val username: String,
    val texto: String,
    val criadoEm: LocalDateTime
)