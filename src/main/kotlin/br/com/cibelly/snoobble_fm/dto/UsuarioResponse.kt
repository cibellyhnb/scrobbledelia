package br.com.cibelly.scrobbledelia.dto

import java.util.UUID

data class UsuarioResponse(
    val id: UUID,
    val nome: String,
    val email: String,
    val username: String,
    val fotoPerfilUrl: String?
)