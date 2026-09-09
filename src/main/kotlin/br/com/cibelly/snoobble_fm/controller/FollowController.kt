package br.com.cibelly.scrobbledelia.controller

import br.com.cibelly.scrobbledelia.service.FollowService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import br.com.cibelly.scrobbledelia.dto.SeguidorResponse
import java.util.UUID

@RestController
@RequestMapping("/usuarios")
class FollowController(
    private val followService: FollowService
) {

    @PostMapping("/{id}/seguir")
    fun seguir(@PathVariable id: UUID): ResponseEntity<Void> {
        val seguidorId = usuarioAutenticadoId()
        followService.seguir(seguidorId, id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}/seguir")
    fun deixarDeSeguir(@PathVariable id: UUID): ResponseEntity<Void> {
        val seguidorId = usuarioAutenticadoId()
        followService.deixarDeSeguir(seguidorId, id)
        return ResponseEntity.noContent().build()
    }

    private fun usuarioAutenticadoId(): UUID {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        return principal as UUID
    }

    @GetMapping("/{id}/seguindo")
    fun listarSeguindo(@PathVariable id: UUID, pageable: Pageable): ResponseEntity<Page<SeguidorResponse>> {
        return ResponseEntity.ok(followService.listarSeguindo(id, pageable))
    }

    @GetMapping("/{id}/seguidores")
    fun listarSeguidores(@PathVariable id: UUID, pageable: Pageable): ResponseEntity<Page<SeguidorResponse>> {
        return ResponseEntity.ok(followService.listarSeguidores(id, pageable))
    }

    @GetMapping("/{id}/mutuos")
    fun listarMutuos(@PathVariable id: UUID, pageable: Pageable): ResponseEntity<Page<SeguidorResponse>> {
        return ResponseEntity.ok(followService.listarMutuos(id, pageable))
    }
}