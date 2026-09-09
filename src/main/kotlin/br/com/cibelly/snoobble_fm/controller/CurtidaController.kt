package br.com.cibelly.scrobbledelia.controller

import br.com.cibelly.scrobbledelia.service.CurtidaService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/posts")
class CurtidaController(
    private val curtidaService: CurtidaService
) {

    @PostMapping("/{id}/curtir")
    fun curtir(@PathVariable id: UUID): ResponseEntity<Void> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        curtidaService.curtir(usuarioId, id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}/curtir")
    fun descurtir(@PathVariable id: UUID): ResponseEntity<Void> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        curtidaService.descurtir(usuarioId, id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}/curtidas")
    fun contarCurtidas(@PathVariable id: UUID): ResponseEntity<Map<String, Long>> {
        val total = curtidaService.contarCurtidas(id)
        return ResponseEntity.ok(mapOf("total" to total))
    }
}