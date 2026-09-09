package br.com.cibelly.scrobbledelia.repository

import br.com.cibelly.scrobbledelia.entity.Curtida
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CurtidaRepository : JpaRepository<Curtida, UUID> {
    fun existsByUsuarioIdAndPostId(usuarioId: UUID, postId: UUID): Boolean
    fun findByUsuarioIdAndPostId(usuarioId: UUID, postId: UUID): Curtida?
    fun countByPostId(postId: UUID): Long
}