package br.com.cibelly.scrobbledelia.dto

import java.time.LocalDateTime

data class ScrobbleResponse(
    val musica: String,
    val artista: String,
    val album: String?,
    val tocadoEm: LocalDateTime
)