package br.com.cibelly.scrobbledelia.controller

import br.com.cibelly.scrobbledelia.dto.NotificacaoResponse
import br.com.cibelly.scrobbledelia.service.NotificacaoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/notificacoes")
class NotificacaoController(
    private val notificacaoService: NotificacaoService
) {

    @GetMapping
    fun listar(pageable: Pageable): ResponseEntity<Page<NotificacaoResponse>> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        return ResponseEntity.ok(notificacaoService.listar(usuarioId, pageable))
    }

    @PatchMapping("/{id}/lida")
    fun marcarComoLida(@PathVariable id: UUID): ResponseEntity<Void> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        notificacaoService.marcarComoLida(usuarioId, id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/nao-lidas/contagem")
    fun contarNaoLidas(): ResponseEntity<Map<String, Long>> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        val total = notificacaoService.contarNaoLidas(usuarioId)
        return ResponseEntity.ok(mapOf("total" to total))
    }
}