package br.com.cibelly.scrobbledelia.dto

import java.util.UUID

data class PostFeedData(
    val postId: UUID,
    val texto: String,
    val musica: String,
    val artista: String,
    val curtidas: Long
)