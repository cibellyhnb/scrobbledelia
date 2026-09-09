package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.dto.*
import br.com.cibelly.scrobbledelia.repository.ScrobbleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import tools.jackson.databind.json.JsonMapper
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

@Service
class EstatisticasService(
    private val scrobbleRepository: ScrobbleRepository,
    private val redisTemplate: StringRedisTemplate,
    private val jsonMapper: JsonMapper
) {

    fun topArtistas(usuarioId: UUID, from: LocalDateTime?, to: LocalDateTime?, limit: Int): List<TopArtistaResponse> {
        return scrobbleRepository.buscarTopArtistas(usuarioId, from, to, PageRequest.of(0, limit))
            .map { TopArtistaResponse(artista = it.getArtista(), total = it.getTotal()) }
    }

    fun topMusicas(usuarioId: UUID, from: LocalDateTime?, to: LocalDateTime?, limit: Int): List<TopMusicaResponse> {
        return scrobbleRepository.buscarTopMusicas(usuarioId, from, to, PageRequest.of(0, limit))
            .map { TopMusicaResponse(artista = it.getArtista(), musica = it.getMusica(), total = it.getTotal()) }
    }

    fun estatisticasGerais(usuarioId: UUID, from: LocalDateTime?, to: LocalDateTime?): EstatisticasGeraisResponse {
        val total = scrobbleRepository.contarScrobbles(usuarioId, from, to)
        val artistas = scrobbleRepository.contarArtistasUnicos(usuarioId, from, to)
        val musicas = scrobbleRepository.contarMusicasUnicas(usuarioId, from, to)

        val distribuicao = scrobbleRepository.buscarDistribuicaoPorHora(usuarioId, from, to, PageRequest.of(0, 1))
        val horaMaisAtiva = distribuicao.firstOrNull()?.getHora()

        return EstatisticasGeraisResponse(
            totalScrobbles = total,
            artistasUnicos = artistas,
            musicasUnicas = musicas,
            horaMaisAtiva = horaMaisAtiva
        )
    }

    fun wrapped(usuarioId: UUID, from: LocalDateTime?, to: LocalDateTime?): WrappedResponse {
        val chaveCache = "wrapped:$usuarioId:${from ?: "inicio"}:${to ?: "fim"}"

        val cacheado = redisTemplate.opsForValue().get(chaveCache)
        if (cacheado != null) {
            return jsonMapper.readValue(cacheado, WrappedResponse::class.java)
        }

        val resumo = estatisticasGerais(usuarioId, from, to)
        val artistas = topArtistas(usuarioId, from, to, 5)
        val musicas = topMusicas(usuarioId, from, to, 5)

        val destaquesEncontrados = scrobbleRepository.buscarDestaques(usuarioId, from, to, PageRequest.of(0, 5))
            .map {
                ScrobbleDestaqueResponse(
                    musica = it.musica,
                    artista = it.artista,
                    motivoDestaque = it.motivoDestaque,
                    tocadoEm = it.tocadoEm.toString()
                )
            }

        val resposta = WrappedResponse(
            periodo = PeriodoResponse(from = from?.toString(), to = to?.toString()),
            resumo = resumo,
            topArtistas = artistas,
            topMusicas = musicas,
            destaques = destaquesEncontrados
        )

        val json = jsonMapper.writeValueAsString(resposta)
        redisTemplate.opsForValue().set(chaveCache, json, Duration.ofMinutes(10))

        return resposta
    }
}