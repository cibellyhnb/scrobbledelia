package br.com.cibelly.scrobbledelia.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CadastroRequest(
    @field:NotBlank(message = "Nome é obrigatório")
    val nome: String,

    @field:NotBlank(message = "Email é obrigatório")
    @field:Email(message = "Email inválido")
    val email: String,

    @field:NotBlank(message = "Senha é obrigatória")
    @field:Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    val senha: String,

    @field:NotBlank(message = "Username é obrigatório")
    @field:Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
    val username: String
)