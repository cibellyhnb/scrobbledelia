package br.com.cibelly.scrobbledelia.dto

data class LoginResponse(
    val token: String,
    val tipo: String = "Bearer"
)