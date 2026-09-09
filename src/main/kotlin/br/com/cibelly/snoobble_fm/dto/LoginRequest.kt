package br.com.cibelly.scrobbledelia.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Email é obrigatório")
    val email: String,

    @field:NotBlank(message = "Senha é obrigatória")
    val senha: String
)