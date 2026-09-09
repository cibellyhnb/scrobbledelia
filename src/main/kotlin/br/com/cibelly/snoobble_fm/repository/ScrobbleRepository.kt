package br.com.cibelly.scrobbledelia.repository

import br.com.cibelly.scrobbledelia.entity.Scrobble
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ScrobbleRepository : JpaRepository<Scrobble, UUID> {
    fun existsByUsuarioIdAndArtistaAndMusicaAndTocadoEm(
        usuarioId: UUID,
        artista: String,
        musica: String,
        tocadoEm: LocalDateTime
    ): Boolean

    fun findByUsuarioIdOrderByTocadoEmDesc(usuarioId: UUID, pageable: Pageable): Page<Scrobble>

    @Query("""
        SELECT s FROM Scrobble s
        JOIN FETCH s.usuario
        WHERE s.usuario.id IN (
            SELECT f.seguido.id FROM Follow f WHERE f.seguidor.id = :usuarioId
        )
        AND (CAST(:cursor AS timestamp) IS NULL OR s.tocadoEm < :cursor)
        ORDER BY s.tocadoEm DESC
    """)
    fun buscarFeed(
        @Param("usuarioId") usuarioId: UUID,
        @Param("cursor") cursor: LocalDateTime?,
        pageable: Pageable
    ): List<Scrobble>

    fun countByUsuarioIdAndArtistaAndMusicaAndTocadoEmBetween(
        usuarioId: UUID,
        artista: String,
        musica: String,
        inicio: LocalDateTime,
        fim: LocalDateTime
    ): Long

    fun countByUsuarioIdAndArtista(usuarioId: UUID, artista: String): Long

    @Query("""
        SELECT s.artista as artista, COUNT(s) as total
        FROM Scrobble s
        WHERE s.usuario.id = :usuarioId
        AND (CAST(:from AS date) IS NULL OR s.tocadoEm >= :from)
        AND (CAST(:to AS date) IS NULL OR s.tocadoEm <= :to)
        GROUP BY s.artista
        ORDER BY COUNT(s) DESC
    """)
    fun buscarTopArtistas(
        @Param("usuarioId") usuarioId: UUID,
        @Param("from") from: LocalDateTime?,
        @Param("to") to: LocalDateTime?,
        pageable: Pageable
    ): List<TopArtistaProjection>

    @Query("""
        SELECT s.artista as artista, s.musica as musica, COUNT(s) as total
        FROM Scrobble s
        WHERE s.usuario.id = :usuarioId
        AND (CAST(:from AS date) IS NULL OR s.tocadoEm >= :from)
        AND (CAST(:to AS date) IS NULL OR s.tocadoEm <= :to)
        GROUP BY s.artista, s.musica
        ORDER BY COUNT(s) DESC
    """)
    fun buscarTopMusicas(
        @Param("usuarioId") usuarioId: UUID,
        @Param("from") from: LocalDateTime?,
        @Param("to") to: LocalDateTime?,
        pageable: Pageable
    ): List<TopMusicaProjection>

    @Query("""
        SELECT COUNT(s) FROM Scrobble s
        WHERE s.usuario.id = :usuarioId
        AND (CAST(:from AS date) IS NULL OR s.tocadoEm >= :from)
        AND (CAST(:to AS date) IS NULL OR s.tocadoEm <= :to)
    """)
    fun contarScrobbles(
        @Param("usuarioId") usuarioId: UUID,
        @Param("from") from: LocalDateTime?,
        @Param("to") to: LocalDateTime?
    ): Long


    @Query("""
        SELECT COUNT(DISTINCT s.artista) FROM Scrobble s
        WHERE s.usuario.id = :usuarioId
        AND (CAST(:from AS date) IS NULL OR s.tocadoEm >= :from)
        AND (CAST(:to AS date) IS NULL OR s.tocadoEm <= :to)
    """)
    fun contarArtistasUnicos(
        @Param("usuarioId") usuarioId: UUID,
        @Param("from") from: LocalDateTime?,
        @Param("to") to: LocalDateTime?
    ): Long

    @Query("""
        SELECT COUNT(DISTINCT CONCAT(s.artista, '|', s.musica)) FROM Scrobble s
        WHERE s.usuario.id = :usuarioId
        AND (CAST(:from AS date) IS NULL OR s.tocadoEm >= :from)
        AND (CAST(:to AS date) IS NULL OR s.tocadoEm <= :to)
    """)
    fun contarMusicasUnicas(
        @Param("usuarioId") usuarioId: UUID,
        @Param("from") from: LocalDateTime?,
        @Param("to") to: LocalDateTime?
    ): Long

    @Query("""
        SELECT EXTRACT(HOUR FROM s.tocadoEm) as hora, COUNT(s) as total
        FROM Scrobble s
        WHERE s.usuario.id = :usuarioId
        AND (CAST(:from AS date) IS NULL OR s.tocadoEm >= :from)
        AND (CAST(:to AS date) IS NULL OR s.tocadoEm <= :to)
        GROUP BY EXTRACT(HOUR FROM s.tocadoEm)
        ORDER BY COUNT(s) DESC
    """)
    fun buscarDistribuicaoPorHora(
        @Param("usuarioId") usuarioId: UUID,
        @Param("from") from: LocalDateTime?,
        @Param("to") to: LocalDateTime?,
        pageable: Pageable
    ): List<HoraProjection>

    @Query("""
        SELECT s FROM Scrobble s
        WHERE s.usuario.id = :usuarioId
        AND s.destaque = true
        AND (CAST(:from AS date) IS NULL OR s.tocadoEm >= :from)
        AND (CAST(:to AS date) IS NULL OR s.tocadoEm <= :to)
        ORDER BY s.tocadoEm DESC
    """)
    fun buscarDestaques(
        @Param("usuarioId") usuarioId: UUID,
        @Param("from") from: LocalDateTime?,
        @Param("to") to: LocalDateTime?,
        pageable: Pageable
    ): List<Scrobble>
}