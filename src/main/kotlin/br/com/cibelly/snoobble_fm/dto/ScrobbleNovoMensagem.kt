package br.com.cibelly.scrobbledelia.dto

import java.io.Serializable
import java.util.UUID

data class ScrobbleNovoMensagem(
    val usuarioId: UUID,
    val artista: String,
    val musica: String
) : Serializable