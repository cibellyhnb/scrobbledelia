package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.entity.Curtida
import br.com.cibelly.scrobbledelia.exception.CurtidaJaExisteException
import br.com.cibelly.scrobbledelia.exception.PostNaoEncontradoException
import br.com.cibelly.scrobbledelia.exception.UsuarioNaoEncontradoException
import br.com.cibelly.scrobbledelia.repository.CurtidaRepository
import br.com.cibelly.scrobbledelia.repository.PostRepository
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CurtidaService(
    private val curtidaRepository: CurtidaRepository,
    private val postRepository: PostRepository,
    private val usuarioRepository: UsuarioRepository
) {

    fun curtir(usuarioId: UUID, postId: UUID) {
        if (curtidaRepository.existsByUsuarioIdAndPostId(usuarioId, postId)) {
            throw CurtidaJaExisteException()
        }

        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { UsuarioNaoEncontradoException(usuarioId) }
        val post = postRepository.findById(postId)
            .orElseThrow { PostNaoEncontradoException(postId) }

        curtidaRepository.save(Curtida(usuario = usuario, post = post))
    }

    fun descurtir(usuarioId: UUID, postId: UUID) {
        val curtida = curtidaRepository.findByUsuarioIdAndPostId(usuarioId, postId)
            ?: return
        curtidaRepository.delete(curtida)
    }

    fun contarCurtidas(postId: UUID): Long {
        return curtidaRepository.countByPostId(postId)
    }
}