package br.com.cibelly.scrobbledelia.service

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import br.com.cibelly.scrobbledelia.repository.ScrobbleRepository

class DestaqueCalculatorTest {

    private val scrobbleRepository: ScrobbleRepository = mockk()
    private val calculator = DestaqueCalculator(scrobbleRepository)

    @Test
    fun `deve marcar como destaque por repeticao quando ja tocou 4 ou mais vezes hoje`() {
        val usuarioId = UUID.randomUUID()
        val agora = LocalDateTime.of(2026, 8, 19, 14, 0, 0)

        every {
            scrobbleRepository.countByUsuarioIdAndArtistaAndMusicaAndTocadoEmBetween(any(), any(), any(), any(), any())
        } returns 3L
        every { scrobbleRepository.countByUsuarioIdAndArtista(any(), any()) } returns 10L

        val (destaque, motivo) = calculator.calcular(usuarioId, "Artista X", "Musica Y", agora)

        assertEquals(true, destaque)
        assertEquals("repeticao", motivo)
    }

    @Test
    fun `deve marcar como destaque por madrugada quando toca entre 2h e 5h`() {
        val usuarioId = UUID.randomUUID()
        val madrugada = LocalDateTime.of(2026, 8, 19, 3, 30, 0)

        every {
            scrobbleRepository.countByUsuarioIdAndArtistaAndMusicaAndTocadoEmBetween(any(), any(), any(), any(), any())
        } returns 0L
        every { scrobbleRepository.countByUsuarioIdAndArtista(any(), any()) } returns 10L

        val (destaque, motivo) = calculator.calcular(usuarioId, "Artista X", "Musica Y", madrugada)

        assertEquals(true, destaque)
        assertEquals("madrugada", motivo)
    }

    @Test
    fun `deve marcar como destaque por artista raro quando tem poucos scrobbles daquele artista`() {
        val usuarioId = UUID.randomUUID()
        val agora = LocalDateTime.of(2026, 8, 19, 14, 0, 0)

        every {
            scrobbleRepository.countByUsuarioIdAndArtistaAndMusicaAndTocadoEmBetween(any(), any(), any(), any(), any())
        } returns 0L
        every { scrobbleRepository.countByUsuarioIdAndArtista(any(), any()) } returns 1L

        val (destaque, motivo) = calculator.calcular(usuarioId, "Artista Novo", "Musica Y", agora)

        assertEquals(true, destaque)
        assertEquals("artista_raro", motivo)
    }

    @Test
    fun `nao deve marcar como destaque quando nenhum criterio bate`() {
        val usuarioId = UUID.randomUUID()
        val horarioNormal = LocalDateTime.of(2026, 8, 19, 14, 0, 0)

        every {
            scrobbleRepository.countByUsuarioIdAndArtistaAndMusicaAndTocadoEmBetween(any(), any(), any(), any(), any())
        } returns 0L
        every { scrobbleRepository.countByUsuarioIdAndArtista(any(), any()) } returns 10L

        val (destaque, motivo) = calculator.calcular(usuarioId, "Artista Comum", "Musica Y", horarioNormal)

        assertEquals(false, destaque)
        assertEquals(null, motivo)
    }
}