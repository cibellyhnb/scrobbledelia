package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.entity.Scrobble
import br.com.cibelly.scrobbledelia.exception.LastFmNaoConectadoException
import br.com.cibelly.scrobbledelia.exception.UsuarioNaoEncontradoException
import br.com.cibelly.scrobbledelia.repository.ScrobbleRepository
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.util.UUID
import br.com.cibelly.scrobbledelia.dto.ScrobbleResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import org.springframework.data.domain.PageRequest
import br.com.cibelly.scrobbledelia.dto.ScrobbleNovoMensagem

@Service
class ScrobbleService(
    private val scrobbleRepository: ScrobbleRepository,
    private val usuarioRepository: UsuarioRepository,
    private val lastFmClient: LastFmClient,
    private val destaqueCalculator: DestaqueCalculator,
    private val rabbitPublisher: RabbitPublisher
) {

    fun sincronizar(usuarioId: UUID): Int {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNaoEncontradoException(usuarioId) }

        val lastfmUsername = usuario.lastfmUsername
            ?: throw LastFmNaoConectadoException()

        val fromEpoch = usuario.ultimaSincronizacao
            ?.atZone(ZoneId.systemDefault())
            ?.toEpochSecond()

        val tracks = lastFmClient.buscarTodosScrobbles(lastfmUsername, from = fromEpoch)

        var salvos = 0

        for (track in tracks) {
            if (track.date == null) continue

            val tocadoEm = Instant.ofEpochSecond(track.date.uts.toLong())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

            val jaExiste = scrobbleRepository.existsByUsuarioIdAndArtistaAndMusicaAndTocadoEm(
                usuarioId, track.artist.text, track.name, tocadoEm
            )
            if (jaExiste) continue

            val (destaque, motivo) = destaqueCalculator.calcular(
                usuarioId, track.artist.text, track.name, tocadoEm
            )

            val scrobble = Scrobble(
                usuario = usuario,
                musica = track.name,
                artista = track.artist.text,
                album = track.album.text.ifBlank { null },
                tocadoEm = tocadoEm,
                destaque = destaque,
                motivoDestaque = motivo
            )
            scrobbleRepository.save(scrobble)
            salvos++

            rabbitPublisher.publicarScrobbleNovo(
                ScrobbleNovoMensagem(
                    usuarioId = usuarioId,
                    artista = scrobble.artista,
                    musica = scrobble.musica
                )
            )
        }

        usuario.ultimaSincronizacao = LocalDateTime.now()
        usuarioRepository.save(usuario)

        return salvos
    }

    fun listarScrobbles(usuarioId: UUID, pageable: Pageable): Page<ScrobbleResponse> {
        return scrobbleRepository.findByUsuarioIdOrderByTocadoEmDesc(usuarioId, pageable)
            .map { it.paraResponse() }
    }

    private fun Scrobble.paraResponse() = ScrobbleResponse(
        musica = this.musica,
        artista = this.artista,
        album = this.album,
        tocadoEm = this.tocadoEm
    )


}