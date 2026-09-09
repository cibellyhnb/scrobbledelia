package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.dto.CadastroRequest
import br.com.cibelly.scrobbledelia.entity.Usuario
import br.com.cibelly.scrobbledelia.exception.EmailJaCadastradoException
import br.com.cibelly.scrobbledelia.exception.UsernameJaCadastradoException
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

class UsuarioServiceTest {

    private val usuarioRepository: UsuarioRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val tokenService: TokenService = mockk()

    private lateinit var usuarioService: UsuarioService

    @BeforeEach
    fun setup() {
        usuarioService = UsuarioService(usuarioRepository, passwordEncoder, tokenService)
    }

    @Test
    fun `deve cadastrar usuario com sucesso quando email e username sao unicos`() {
        val request = CadastroRequest(
            nome = "Teste",
            email = "teste@teste.com",
            senha = "senha12345",
            username = "testeuser"
        )

        every { usuarioRepository.existsByEmail(request.email) } returns false
        every { usuarioRepository.existsByUsername(request.username) } returns false
        every { passwordEncoder.encode(request.senha) } returns "hash-fake"
        every { usuarioRepository.save(any()) } answers {
            firstArg<Usuario>().let {
                Usuario(
                    id = UUID.randomUUID(),
                    nome = it.nome,
                    email = it.email,
                    senhaHash = it.senhaHash,
                    username = it.username
                )
            }
        }

        val response = usuarioService.cadastrar(request)

        assert(response.email == request.email)
        assert(response.username == request.username)
        verify { usuarioRepository.save(any()) }
    }

    @Test
    fun `deve lancar excecao quando email ja esta cadastrado`() {
        val request = CadastroRequest(
            nome = "Teste",
            email = "existente@teste.com",
            senha = "senha12345",
            username = "novouser"
        )

        every { usuarioRepository.existsByEmail(request.email) } returns true

        assertThrows(EmailJaCadastradoException::class.java) {
            usuarioService.cadastrar(request)
        }
    }

    @Test
    fun `deve lancar excecao quando username ja esta cadastrado`() {
        val request = CadastroRequest(
            nome = "Teste",
            email = "novo@teste.com",
            senha = "senha12345",
            username = "existente"
        )

        every { usuarioRepository.existsByEmail(request.email) } returns false
        every { usuarioRepository.existsByUsername(request.username) } returns true

        assertThrows(UsernameJaCadastradoException::class.java) {
            usuarioService.cadastrar(request)
        }
    }
}