package br.com.cibelly.scrobbledelia.controller

import br.com.cibelly.scrobbledelia.dto.TopArtistaResponse
import br.com.cibelly.scrobbledelia.dto.TopMusicaResponse
import br.com.cibelly.scrobbledelia.service.EstatisticasService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.UUID
import br.com.cibelly.scrobbledelia.dto.EstatisticasGeraisResponse
import br.com.cibelly.scrobbledelia.dto.WrappedResponse

@RestController
@RequestMapping("/estatisticas/me")
class EstatisticasController(
    private val estatisticasService: EstatisticasService
) {

    @GetMapping("/top-artistas")
    fun topArtistas(
        @RequestParam(required = false) from: LocalDateTime?,
        @RequestParam(required = false) to: LocalDateTime?,
        @RequestParam(defaultValue = "5") limit: Int
    ): ResponseEntity<List<TopArtistaResponse>> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        val limiteSeguro = limit.coerceIn(1, 20)
        return ResponseEntity.ok(estatisticasService.topArtistas(usuarioId, from, to, limiteSeguro))
    }

    @GetMapping("/top-musicas")
    fun topMusicas(
        @RequestParam(required = false) from: LocalDateTime?,
        @RequestParam(required = false) to: LocalDateTime?,
        @RequestParam(defaultValue = "5") limit: Int
    ): ResponseEntity<List<TopMusicaResponse>> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        val limiteSeguro = limit.coerceIn(1, 20)
        return ResponseEntity.ok(estatisticasService.topMusicas(usuarioId, from, to, limiteSeguro))
    }

    @GetMapping("/gerais")
    fun gerais(
        @RequestParam(required = false) from: LocalDateTime?,
        @RequestParam(required = false) to: LocalDateTime?
    ): ResponseEntity<EstatisticasGeraisResponse> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        return ResponseEntity.ok(estatisticasService.estatisticasGerais(usuarioId, from, to))
    }

    @GetMapping("/wrapped")
    fun wrapped(
        @RequestParam(required = false) from: LocalDateTime?,
        @RequestParam(required = false) to: LocalDateTime?
    ): ResponseEntity<WrappedResponse> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        return ResponseEntity.ok(estatisticasService.wrapped(usuarioId, from, to))
    }
}