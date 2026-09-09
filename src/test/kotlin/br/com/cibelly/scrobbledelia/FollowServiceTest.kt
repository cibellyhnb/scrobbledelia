package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.entity.Follow
import br.com.cibelly.scrobbledelia.entity.Usuario
import br.com.cibelly.scrobbledelia.exception.JaSeguindoException
import br.com.cibelly.scrobbledelia.exception.SeguirASiMesmoException
import br.com.cibelly.scrobbledelia.repository.FollowRepository
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

class FollowServiceTest {

    private val followRepository: FollowRepository = mockk()
    private val usuarioRepository: UsuarioRepository = mockk()

    private lateinit var followService: FollowService

    @BeforeEach
    fun setup() {
        followService = FollowService(followRepository, usuarioRepository)
    }

    @Test
    fun `deve lancar excecao ao tentar seguir a si mesmo`() {
        val usuarioId = UUID.randomUUID()

        assertThrows(SeguirASiMesmoException::class.java) {
            followService.seguir(usuarioId, usuarioId)
        }
    }

    @Test
    fun `deve lancar excecao ao tentar seguir alguem que ja segue`() {
        val seguidorId = UUID.randomUUID()
        val seguidoId = UUID.randomUUID()

        every { followRepository.existsBySeguidorIdAndSeguidoId(seguidorId, seguidoId) } returns true

        assertThrows(JaSeguindoException::class.java) {
            followService.seguir(seguidorId, seguidoId)
        }
    }

    @Test
    fun `deve seguir com sucesso quando nao ha follow existente`() {
        val seguidorId = UUID.randomUUID()
        val seguidoId = UUID.randomUUID()

        val seguidor = Usuario(id = seguidorId, nome = "A", email = "a@teste.com", senhaHash = "hash", username = "usera")
        val seguido = Usuario(id = seguidoId, nome = "B", email = "b@teste.com", senhaHash = "hash", username = "userb")

        every { followRepository.existsBySeguidorIdAndSeguidoId(seguidorId, seguidoId) } returns false
        every { usuarioRepository.findById(seguidorId) } returns Optional.of(seguidor)
        every { usuarioRepository.findById(seguidoId) } returns Optional.of(seguido)
        every { followRepository.save(any()) } returns Follow(seguidor = seguidor, seguido = seguido)

        followService.seguir(seguidorId, seguidoId)

        verify { followRepository.save(any()) }
    }
}