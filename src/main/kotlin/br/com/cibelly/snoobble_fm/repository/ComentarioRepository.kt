package br.com.cibelly.scrobbledelia.repository

import br.com.cibelly.scrobbledelia.entity.Comentario
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ComentarioRepository : JpaRepository<Comentario, UUID> {
    fun findByPostIdOrderByCriadoEmAsc(postId: UUID, pageable: Pageable): Page<Comentario>
}