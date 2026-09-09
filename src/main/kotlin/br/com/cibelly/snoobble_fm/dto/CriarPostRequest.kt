package br.com.cibelly.scrobbledelia.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class CriarPostRequest(
    val scrobbleId: UUID,

    @field:NotBlank(message = "Texto não pode ser vazio")
    @field:Size(max = 500, message = "Texto deve ter no máximo 500 caracteres")
    val texto: String
)