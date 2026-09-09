package br.com.cibelly.scrobbledelia.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "usuarios")
class Usuario(

    @Column(nullable = false, length = 100)
    var nome: String,

    @Column(nullable = false, unique = true, length = 150)
    var email: String,

    @Column(name = "senha_hash", nullable = false)
    var senhaHash: String,

    @Column(nullable = false, unique = true, length = 50)
    var username: String,

    @Column(name = "foto_perfil_url", length = 500)
    var fotoPerfilUrl: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "criado_em", nullable = false, updatable = false)
    val criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "lastfm_username", length = 50)
    var lastfmUsername: String? = null,

    @Column(name = "ultima_sincronizacao")
    var ultimaSincronizacao: LocalDateTime? = null,

    @Column(length = 100)
    var cidade: String? = null,

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Usuario) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}