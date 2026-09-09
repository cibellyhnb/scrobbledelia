package br.com.cibelly.scrobbledelia.dto

import java.util.UUID

data class SeguidorResponse(
    val id: UUID,
    val nome: String,
    val username: String,
    val fotoPerfilUrl: String?
)