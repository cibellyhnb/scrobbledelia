package br.com.cibelly.scrobbledelia.controller

import br.com.cibelly.scrobbledelia.dto.ComentarioResponse
import br.com.cibelly.scrobbledelia.dto.CriarComentarioRequest
import br.com.cibelly.scrobbledelia.service.ComentarioService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class ComentarioController(
    private val comentarioService: ComentarioService
) {

    @PostMapping("/posts/{postId}/comentarios")
    fun criar(
        @PathVariable postId: UUID,
        @Valid @RequestBody request: CriarComentarioRequest
    ): ResponseEntity<ComentarioResponse> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        val response = comentarioService.criar(usuarioId, postId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/posts/{postId}/comentarios")
    fun listar(@PathVariable postId: UUID, pageable: Pageable): ResponseEntity<Page<ComentarioResponse>> {
        return ResponseEntity.ok(comentarioService.listar(postId, pageable))
    }

    @DeleteMapping("/comentarios/{id}")
    fun deletar(@PathVariable id: UUID): ResponseEntity<Void> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        comentarioService.deletar(usuarioId, id)
        return ResponseEntity.noContent().build()
    }
}