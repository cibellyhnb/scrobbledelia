package br.com.cibelly.scrobbledelia.controller

import br.com.cibelly.scrobbledelia.dto.CriarPostRequest
import br.com.cibelly.scrobbledelia.dto.PostResponse
import br.com.cibelly.scrobbledelia.service.PostService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {

    @PostMapping
    fun criar(@Valid @RequestBody request: CriarPostRequest): ResponseEntity<PostResponse> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        val response = postService.criar(usuarioId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: UUID): ResponseEntity<Void> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        postService.deletar(usuarioId, id)
        return ResponseEntity.noContent().build()
    }
}