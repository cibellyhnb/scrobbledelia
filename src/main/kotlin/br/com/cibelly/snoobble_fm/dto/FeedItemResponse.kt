package br.com.cibelly.scrobbledelia.dto

import java.time.LocalDateTime
import java.util.UUID

data class FeedItemResponse(
    val tipo: String,
    val usuarioId: UUID,
    val username: String,
    val timestamp: LocalDateTime,
    val dados: Any
)