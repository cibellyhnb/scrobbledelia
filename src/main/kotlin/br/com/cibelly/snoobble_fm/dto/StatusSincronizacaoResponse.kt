package br.com.cibelly.scrobbledelia.dto

import java.time.LocalDateTime

data class StatusSincronizacaoResponse(
    val lastfmConectado: Boolean,
    val lastfmUsername: String?,
    val ultimaSincronizacao: LocalDateTime?
)