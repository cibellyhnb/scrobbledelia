package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.dto.CriarPostRequest
import br.com.cibelly.scrobbledelia.dto.PostResponse
import br.com.cibelly.scrobbledelia.entity.Post
import br.com.cibelly.scrobbledelia.exception.PostNaoEncontradoException
import br.com.cibelly.scrobbledelia.exception.SemPermissaoException
import br.com.cibelly.scrobbledelia.exception.UsuarioNaoEncontradoException
import br.com.cibelly.scrobbledelia.repository.PostRepository
import br.com.cibelly.scrobbledelia.repository.ScrobbleRepository
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import org.springframework.stereotype.Service
import br.com.cibelly.scrobbledelia.exception.ScrobbleNaoEncontradoException
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val scrobbleRepository: ScrobbleRepository,
    private val usuarioRepository: UsuarioRepository
) {

    fun criar(usuarioId: UUID, request: CriarPostRequest): PostResponse {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNaoEncontradoException(usuarioId) }

        val scrobble = scrobbleRepository.findById(request.scrobbleId)
            .orElseThrow { ScrobbleNaoEncontradoException(request.scrobbleId) }

        val post = Post(
            usuario = usuario,
            scrobble = scrobble,
            texto = request.texto
        )

        val salvo = postRepository.save(post)

        return salvo.paraResponse()
    }

    fun deletar(usuarioId: UUID, postId: UUID) {
        val post = postRepository.findById(postId)
            .orElseThrow { PostNaoEncontradoException(postId) }

        if (post.usuario.id != usuarioId) {
            throw SemPermissaoException("Você só pode deletar seus próprios posts")
        }

        postRepository.delete(post)
    }

    private fun Post.paraResponse() = PostResponse(
        id = this.id!!,
        usuarioId = this.usuario.id!!,
        username = this.usuario.username,
        texto = this.texto,
        musica = this.scrobble.musica,
        artista = this.scrobble.artista,
        criadoEm = this.criadoEm
    )
}