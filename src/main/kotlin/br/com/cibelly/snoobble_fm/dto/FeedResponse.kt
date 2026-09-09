package br.com.cibelly.scrobbledelia.dto

import java.time.LocalDateTime

data class FeedResponse(
    val itens: List<FeedItemResponse>,
    val proximoCursor: LocalDateTime?
)