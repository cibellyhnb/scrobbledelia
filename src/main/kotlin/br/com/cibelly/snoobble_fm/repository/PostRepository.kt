package br.com.cibelly.scrobbledelia.repository

import br.com.cibelly.scrobbledelia.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PostRepository : JpaRepository<Post, UUID> {

    @Query("""
        SELECT p FROM Post p
        WHERE p.usuario.id IN (
            SELECT f.seguido.id FROM Follow f WHERE f.seguidor.id = :usuarioId
        )
        ORDER BY p.criadoEm DESC
    """)
    fun buscarFeedDePosts(@Param("usuarioId") usuarioId: UUID): List<Post>
}