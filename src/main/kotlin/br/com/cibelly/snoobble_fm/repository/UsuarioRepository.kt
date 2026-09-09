package br.com.cibelly.scrobbledelia.repository

import br.com.cibelly.scrobbledelia.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UsuarioRepository : JpaRepository<Usuario, UUID> {
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean
    fun findByEmail(email: String): Usuario?
    fun findByLastfmUsernameIsNotNull(): List<Usuario>
}