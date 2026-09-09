package br.com.cibelly.scrobbledelia.dto

import jakarta.validation.constraints.NotBlank

data class ConectarLastFmRequest(
    @field:NotBlank(message = "Username do Last.fm é obrigatório")
    val lastfmUsername: String
)