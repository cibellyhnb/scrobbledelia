package br.com.cibelly.scrobbledelia.controller

import br.com.cibelly.scrobbledelia.dto.CadastroRequest
import br.com.cibelly.scrobbledelia.dto.UsuarioResponse
import br.com.cibelly.scrobbledelia.service.UsuarioService
import br.com.cibelly.scrobbledelia.dto.LoginRequest
import br.com.cibelly.scrobbledelia.dto.LoginResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import br.com.cibelly.scrobbledelia.dto.ConectarLastFmRequest
import java.util.UUID
import org.springframework.web.bind.annotation.*
import br.com.cibelly.scrobbledelia.dto.StatusSincronizacaoResponse
import br.com.cibelly.scrobbledelia.dto.AtualizarLocalizacaoRequest

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody request: CadastroRequest): ResponseEntity<UsuarioResponse> {
        val response = usuarioService.cadastrar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = usuarioService.login(request)
        return ResponseEntity.ok(response)
    }

     @GetMapping("/me")
    fun meuPerfil(): ResponseEntity<String> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal ?: "desconhecido"
        return ResponseEntity.ok("Usuário autenticado: $usuarioId")
    }

    @PutMapping("/me/lastfm")
    fun conectarLastFm(@Valid @RequestBody request: ConectarLastFmRequest): ResponseEntity<Void> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        usuarioService.conectarLastFm(usuarioId, request.lastfmUsername)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/me/lastfm/status")
    fun statusLastFm(): ResponseEntity<StatusSincronizacaoResponse> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        return ResponseEntity.ok(usuarioService.buscarStatusSincronizacao(usuarioId))
    }

    @PutMapping("/me/localizacao")
    fun atualizarLocalizacao(@Valid @RequestBody request: AtualizarLocalizacaoRequest): ResponseEntity<Void> {
        val usuarioId = SecurityContextHolder.getContext().authentication?.principal as UUID
        usuarioService.atualizarLocalizacao(usuarioId, request.cidade)
        return ResponseEntity.noContent().build()
    }
}