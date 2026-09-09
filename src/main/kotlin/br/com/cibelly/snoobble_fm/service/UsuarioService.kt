package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.dto.CadastroRequest
import br.com.cibelly.scrobbledelia.dto.LoginRequest
import br.com.cibelly.scrobbledelia.dto.LoginResponse
import br.com.cibelly.scrobbledelia.dto.UsuarioResponse
import br.com.cibelly.scrobbledelia.entity.Usuario
import br.com.cibelly.scrobbledelia.exception.CredenciaisInvalidasException
import br.com.cibelly.scrobbledelia.exception.EmailJaCadastradoException
import br.com.cibelly.scrobbledelia.exception.UsernameJaCadastradoException
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import br.com.cibelly.scrobbledelia.exception.UsuarioNaoEncontradoException
import br.com.cibelly.scrobbledelia.dto.StatusSincronizacaoResponse
import java.util.UUID

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenService: TokenService
) {

    fun cadastrar(request: CadastroRequest): UsuarioResponse {
        if (usuarioRepository.existsByEmail(request.email)) {
            throw EmailJaCadastradoException(request.email)
        }
        if (usuarioRepository.existsByUsername(request.username)) {
            throw UsernameJaCadastradoException(request.username)
        }

        val usuario = Usuario(
            nome = request.nome,
            email = request.email,
            senhaHash = passwordEncoder.encode(request.senha)!!,
            username = request.username
        )

        val salvo = usuarioRepository.save(usuario)

        return UsuarioResponse(
            id = salvo.id!!,
            nome = salvo.nome,
            email = salvo.email,
            username = salvo.username,
            fotoPerfilUrl = salvo.fotoPerfilUrl
        )
    }

    fun login(request: LoginRequest): LoginResponse {
      val usuario = usuarioRepository.findByEmail(request.email)
          ?: throw CredenciaisInvalidasException()

      if (!passwordEncoder.matches(request.senha, usuario.senhaHash)) {
          throw CredenciaisInvalidasException()
      }

      val token = tokenService.gerarToken(usuario.id!!, usuario.username)
      return LoginResponse(token = token)
    }

    fun conectarLastFm(usuarioId: UUID, lastfmUsername: String) {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNaoEncontradoException(usuarioId) }

        usuario.lastfmUsername = lastfmUsername
        usuarioRepository.save(usuario)
    }

    fun buscarStatusSincronizacao(usuarioId: UUID): StatusSincronizacaoResponse {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNaoEncontradoException(usuarioId) }

        return StatusSincronizacaoResponse(
            lastfmConectado = usuario.lastfmUsername != null,
            lastfmUsername = usuario.lastfmUsername,
            ultimaSincronizacao = usuario.ultimaSincronizacao
        )
    }

    fun atualizarLocalizacao(usuarioId: UUID, cidade: String) {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNaoEncontradoException(usuarioId) }

        usuario.cidade = cidade
        usuarioRepository.save(usuario)
    }
}