package br.com.cibelly.scrobbledelia.dto

import jakarta.validation.constraints.NotBlank

data class AtualizarLocalizacaoRequest(
    @field:NotBlank(message = "Cidade não pode ser vazia")
    val cidade: String
)