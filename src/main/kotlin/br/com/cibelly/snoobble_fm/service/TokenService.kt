package br.com.cibelly.scrobbledelia.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Service
class TokenService(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration-ms}") private val expirationMs: Long
) {

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun gerarToken(usuarioId: UUID, username: String): String {
        val agora = Date()
        val expiracao = Date(agora.time + expirationMs)

        return Jwts.builder()
            .subject(usuarioId.toString())
            .claim("username", username)
            .issuedAt(agora)
            .expiration(expiracao)
            .signWith(key)
            .compact()
    }

    fun extrairUsuarioId(token: String): UUID {
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        return UUID.fromString(claims.subject)
    }

    fun validarToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}