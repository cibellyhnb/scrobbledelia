package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.repository.ScrobbleRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class DestaqueCalculator(
    private val scrobbleRepository: ScrobbleRepository
) {
    private val limiteRepeticao = 4L
    private val horaInicioMadrugada = 1
    private val horaFimMadrugada = 5
    private val limiteArtistaRaro = 5L

    fun calcular(usuarioId: UUID, artista: String, musica: String, tocadoEm: LocalDateTime): Pair<Boolean, String?> {
        val inicioDia = tocadoEm.toLocalDate().atStartOfDay()
        val fimDia = inicioDia.plusDays(1)

        val repeticoesHoje = scrobbleRepository.countByUsuarioIdAndArtistaAndMusicaAndTocadoEmBetween(
            usuarioId, artista, musica, inicioDia, fimDia
        )
        if (repeticoesHoje + 1 >= limiteRepeticao) {
            return true to "repeticao"
        }

        val hora = tocadoEm.hour
        if (hora in horaInicioMadrugada until horaFimMadrugada) {
            return true to "madrugada"
        }

        val scrobblesDoArtista = scrobbleRepository.countByUsuarioIdAndArtista(usuarioId, artista)
        if (scrobblesDoArtista < limiteArtistaRaro) {
            return true to "artista_raro"
        }

        return false to null
    }
}