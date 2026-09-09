package br.com.cibelly.scrobbledelia.controller

import br.com.cibelly.scrobbledelia.dto.ScrobbleResponse
import br.com.cibelly.scrobbledelia.dto.SincronizacaoResponse
import br.com.cibelly.scrobbledelia.service.ScrobbleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.UUID
import br.com.cibelly.scrobbledelia.entity.Scrobble
import br.com.cibelly.scrobbledelia.repository.ScrobbleRepository
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime
import org.springframework.data.domain.PageRequest
import br.com.cibelly.scrobbledelia.dto.FeedResponse
import br.com.cibelly.scrobbledelia.service.DestaqueCalculator
import br.com.cibelly.scrobbledelia.service.SessaoAgrupadora
import br.com.cibelly.scrobbledelia.service.SessaoEscuta
import br.com.cibelly.scrobbledelia.service.FeedService
import br.com.cibelly.scrobbledelia.repository.TopArtistaProjection

@RestController
class ScrobbleController(
    private val scrobbleService: ScrobbleService,
    private val destaqueCalculator: DestaqueCalculator,
    private val scrobbleRepository: ScrobbleRepository,
    private val sessaoAgrupadora: SessaoAgrupadora,
    private val feedService: FeedService
) {

    @PostMapping("/usuarios/me/scrobbles/sincronizar")
    fun sincronizar(): ResponseEntity<SincronizacaoResponse> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        val quantidade = scrobbleService.sincronizar(usuarioId)
        return ResponseEntity.ok(SincronizacaoResponse(scrobblesSalvos = quantidade))
    }

    @GetMapping("/usuarios/{id}/scrobbles")
    fun listarScrobbles(@PathVariable id: UUID, pageable: Pageable): ResponseEntity<Page<ScrobbleResponse>> {
        return ResponseEntity.ok(scrobbleService.listarScrobbles(id, pageable))
    }

    @GetMapping("/feed")
    fun feed(
        @RequestParam(required = false) cursor: LocalDateTime?,
        @RequestParam(defaultValue = "20") limit: Int
    ): ResponseEntity<FeedResponse> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        val limiteSeguro = limit.coerceIn(1, 50)
        return ResponseEntity.ok(feedService.buscarFeed(usuarioId, cursor, limiteSeguro))
    }

    @GetMapping("/destaque/teste")
    fun testarDestaque(
        @RequestParam artista: String,
        @RequestParam musica: String,
        @RequestParam tocadoEm: LocalDateTime
    ): Map<String, Any?> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        val (destaque, motivo) = destaqueCalculator.calcular(usuarioId, artista, musica, tocadoEm)
        return mapOf("destaque" to destaque, "motivo" to motivo)
    }

    @GetMapping("/sessoes/teste")
    fun testarSessoes(): List<SessaoEscuta> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        val scrobbles = scrobbleRepository.buscarFeed(usuarioId, null, PageRequest.of(0, 100))
        return sessaoAgrupadora.agrupar(scrobbles)
    }

}