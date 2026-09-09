package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.entity.Scrobble
import br.com.cibelly.scrobbledelia.entity.Usuario
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class SessaoAgrupadoraTest {

    private val agrupadora = SessaoAgrupadora()

    private fun criarUsuario() = Usuario(
        id = UUID.randomUUID(),
        nome = "Teste",
        email = "teste@teste.com",
        senhaHash = "hash",
        username = "teste"
    )

    private fun criarScrobble(usuario: Usuario, tocadoEm: LocalDateTime) = Scrobble(
        usuario = usuario,
        musica = "Musica",
        artista = "Artista",
        tocadoEm = tocadoEm
    )

    @Test
    fun `deve agrupar scrobbles proximos numa sessao so`() {
        val usuario = criarUsuario()
        val scrobbles = listOf(
            criarScrobble(usuario, LocalDateTime.of(2026, 8, 19, 20, 0, 0)),
            criarScrobble(usuario, LocalDateTime.of(2026, 8, 19, 20, 15, 0)),
            criarScrobble(usuario, LocalDateTime.of(2026, 8, 19, 20, 25, 0))
        )

        val sessoes = agrupadora.agrupar(scrobbles)

        assertEquals(1, sessoes.size)
        assertEquals(3, sessoes.first().quantidade)
    }

    @Test
    fun `deve separar em sessoes distintas quando intervalo passa de 30 minutos`() {
        val usuario = criarUsuario()
        val scrobbles = listOf(
            criarScrobble(usuario, LocalDateTime.of(2026, 8, 19, 20, 0, 0)),
            criarScrobble(usuario, LocalDateTime.of(2026, 8, 19, 23, 0, 0))
        )

        val sessoes = agrupadora.agrupar(scrobbles)

        assertEquals(2, sessoes.size)
        assertEquals(1, sessoes[0].quantidade)
        assertEquals(1, sessoes[1].quantidade)
    }

    @Test
    fun `nao deve misturar sessoes de usuarios diferentes`() {
        val usuarioA = criarUsuario()
        val usuarioB = criarUsuario()
        val scrobbles = listOf(
            criarScrobble(usuarioA, LocalDateTime.of(2026, 8, 19, 20, 0, 0)),
            criarScrobble(usuarioB, LocalDateTime.of(2026, 8, 19, 20, 5, 0))
        )

        val sessoes = agrupadora.agrupar(scrobbles)

        assertEquals(2, sessoes.size)
    }
}