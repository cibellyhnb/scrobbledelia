package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.exception.CurtidaJaExisteException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID
import br.com.cibelly.scrobbledelia.repository.CurtidaRepository
import br.com.cibelly.scrobbledelia.repository.PostRepository
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository

class CurtidaServiceTest {

    private val curtidaRepository: CurtidaRepository = mockk()
    private val postRepository: PostRepository = mockk()
    private val usuarioRepository: UsuarioRepository = mockk()
    private val service = CurtidaService(curtidaRepository, postRepository, usuarioRepository)

    @Test
    fun `deve lancar excecao ao tentar curtir duas vezes`() {
        val usuarioId = UUID.randomUUID()
        val postId = UUID.randomUUID()

        every { curtidaRepository.existsByUsuarioIdAndPostId(usuarioId, postId) } returns true

        assertThrows(CurtidaJaExisteException::class.java) {
            service.curtir(usuarioId, postId)
        }
    }

    @Test
    fun `descurtir nao deve lancar erro quando curtida nao existe`() {
        val usuarioId = UUID.randomUUID()
        val postId = UUID.randomUUID()

        every { curtidaRepository.findByUsuarioIdAndPostId(usuarioId, postId) } returns null

        service.descurtir(usuarioId, postId)

        verify(exactly = 0) { curtidaRepository.delete(any()) }
    }
}