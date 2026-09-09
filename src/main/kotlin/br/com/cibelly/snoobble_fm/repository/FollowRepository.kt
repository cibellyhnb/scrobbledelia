package br.com.cibelly.scrobbledelia.repository

import br.com.cibelly.scrobbledelia.entity.Follow
import br.com.cibelly.scrobbledelia.entity.Usuario
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface FollowRepository : JpaRepository<Follow, UUID> {
    fun existsBySeguidorIdAndSeguidoId(seguidorId: UUID, seguidoId: UUID): Boolean
    fun findBySeguidorIdAndSeguidoId(seguidorId: UUID, seguidoId: UUID): Follow?

    @Query("SELECT f.seguido FROM Follow f WHERE f.seguidor.id = :usuarioId")
    fun buscarSeguindo(@Param("usuarioId") usuarioId: UUID, pageable: Pageable): Page<Usuario>

    @Query("SELECT f.seguidor FROM Follow f WHERE f.seguido.id = :usuarioId")
    fun buscarSeguidores(@Param("usuarioId") usuarioId: UUID, pageable: Pageable): Page<Usuario>

    @Query("""
        SELECT f1.seguido FROM Follow f1
        WHERE f1.seguidor.id = :usuarioId
        AND EXISTS (
            SELECT 1 FROM Follow f2
            WHERE f2.seguidor.id = f1.seguido.id
            AND f2.seguido.id = :usuarioId
        )
    """)
    fun buscarMutuos(@Param("usuarioId") usuarioId: UUID, pageable: Pageable): Page<Usuario>
}