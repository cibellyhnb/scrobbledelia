package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.entity.Follow
import br.com.cibelly.scrobbledelia.exception.JaSeguindoException
import br.com.cibelly.scrobbledelia.exception.SeguirASiMesmoException
import br.com.cibelly.scrobbledelia.exception.UsuarioNaoEncontradoException
import br.com.cibelly.scrobbledelia.repository.FollowRepository
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import org.springframework.stereotype.Service
import br.com.cibelly.scrobbledelia.dto.SeguidorResponse
import br.com.cibelly.scrobbledelia.entity.Usuario
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

@Service
class FollowService(
    private val followRepository: FollowRepository,
    private val usuarioRepository: UsuarioRepository
) {

    fun seguir(seguidorId: UUID, seguidoId: UUID) {
        if (seguidorId == seguidoId) {
            throw SeguirASiMesmoException()
        }

        if (followRepository.existsBySeguidorIdAndSeguidoId(seguidorId, seguidoId)) {
            throw JaSeguindoException()
        }

        val seguidor = usuarioRepository.findById(seguidorId)
            .orElseThrow { UsuarioNaoEncontradoException(seguidorId) }
        val seguido = usuarioRepository.findById(seguidoId)
            .orElseThrow { UsuarioNaoEncontradoException(seguidoId) }

        followRepository.save(Follow(seguidor = seguidor, seguido = seguido))
    }

    fun deixarDeSeguir(seguidorId: UUID, seguidoId: UUID) {
        val follow = followRepository.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId)
            ?: return
        followRepository.delete(follow)
    }

    fun listarSeguindo(usuarioId: UUID, pageable: Pageable): Page<SeguidorResponse> {
        return followRepository.buscarSeguindo(usuarioId, pageable).map { it.paraSeguidorResponse() }
    }

    fun listarSeguidores(usuarioId: UUID, pageable: Pageable): Page<SeguidorResponse> {
        return followRepository.buscarSeguidores(usuarioId, pageable).map { it.paraSeguidorResponse() }
    }

    fun listarMutuos(usuarioId: UUID, pageable: Pageable): Page<SeguidorResponse> {
        return followRepository.buscarMutuos(usuarioId, pageable).map { it.paraSeguidorResponse() }
    }

    private fun Usuario.paraSeguidorResponse() = SeguidorResponse(
        id = this.id!!,
        nome = this.nome,
        username = this.username,
        fotoPerfilUrl = this.fotoPerfilUrl
    )
}