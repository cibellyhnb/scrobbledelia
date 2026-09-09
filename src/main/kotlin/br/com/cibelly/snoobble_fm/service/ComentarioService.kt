package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.dto.ComentarioResponse
import br.com.cibelly.scrobbledelia.dto.CriarComentarioRequest
import br.com.cibelly.scrobbledelia.entity.Comentario
import br.com.cibelly.scrobbledelia.exception.ComentarioNaoEncontradoException
import br.com.cibelly.scrobbledelia.exception.PostNaoEncontradoException
import br.com.cibelly.scrobbledelia.exception.SemPermissaoException
import br.com.cibelly.scrobbledelia.exception.UsuarioNaoEncontradoException
import br.com.cibelly.scrobbledelia.repository.ComentarioRepository
import br.com.cibelly.scrobbledelia.repository.PostRepository
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ComentarioService(
    private val comentarioRepository: ComentarioRepository,
    private val postRepository: PostRepository,
    private val usuarioRepository: UsuarioRepository
) {

    fun criar(usuarioId: UUID, postId: UUID, request: CriarComentarioRequest): ComentarioResponse {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNaoEncontradoException(usuarioId) }
        val post = postRepository.findById(postId)
            .orElseThrow { PostNaoEncontradoException(postId) }

        val comentario = Comentario(
            usuario = usuario,
            post = post,
            texto = request.texto
        )

        val salvo = comentarioRepository.save(comentario)
        return salvo.paraResponse()
    }

    fun listar(postId: UUID, pageable: Pageable): Page<ComentarioResponse> {
        return comentarioRepository.findByPostIdOrderByCriadoEmAsc(postId, pageable)
            .map { it.paraResponse() }
    }

    fun deletar(usuarioId: UUID, comentarioId: UUID) {
        val comentario = comentarioRepository.findById(comentarioId)
            .orElseThrow { ComentarioNaoEncontradoException(comentarioId) }

        if (comentario.usuario.id != usuarioId) {
            throw SemPermissaoException("Você só pode deletar seus próprios comentários")
        }

        comentarioRepository.delete(comentario)
    }

    private fun Comentario.paraResponse() = ComentarioResponse(
        id = this.id!!,
        usuarioId = this.usuario.id!!,
        username = this.usuario.username,
        texto = this.texto,
        criadoEm = this.criadoEm
    )
}