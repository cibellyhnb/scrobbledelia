package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.dto.FeedItemResponse
import br.com.cibelly.scrobbledelia.dto.FeedResponse
import br.com.cibelly.scrobbledelia.dto.PostFeedData
import br.com.cibelly.scrobbledelia.dto.SessaoFeedData
import br.com.cibelly.scrobbledelia.repository.CurtidaRepository
import br.com.cibelly.scrobbledelia.repository.PostRepository
import br.com.cibelly.scrobbledelia.repository.ScrobbleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class FeedService(
    private val scrobbleRepository: ScrobbleRepository,
    private val postRepository: PostRepository,
    private val curtidaRepository: CurtidaRepository,
    private val sessaoAgrupadora: SessaoAgrupadora
) {

    fun buscarFeed(usuarioId: UUID, cursor: LocalDateTime?, limit: Int): FeedResponse {
        val scrobbles = scrobbleRepository.buscarFeed(usuarioId, null, PageRequest.of(0, 500))
        val sessoes = sessaoAgrupadora.agrupar(scrobbles)

        val itensSessao = sessoes.map { sessao ->
            FeedItemResponse(
                tipo = "SESSAO_ESCUTA",
                usuarioId = sessao.usuario.id!!,
                username = sessao.usuario.username,
                timestamp = sessao.fim,
                dados = SessaoFeedData(
                    quantidade = sessao.quantidade,
                    artistasUnicos = sessao.artistasUnicos,
                    destaque = sessao.scrobbleDestaque != null,
                    motivoDestaque = sessao.scrobbleDestaque?.motivoDestaque
                )
            )
        }

        val posts = postRepository.buscarFeedDePosts(usuarioId)
        val itensPost = posts.map { post ->
            FeedItemResponse(
                tipo = "POST",
                usuarioId = post.usuario.id!!,
                username = post.usuario.username,
                timestamp = post.criadoEm,
                dados = PostFeedData(
                    postId = post.id!!,
                    texto = post.texto,
                    musica = post.scrobble.musica,
                    artista = post.scrobble.artista,
                    curtidas = curtidaRepository.countByPostId(post.id!!)
                )
            )
        }

        val todosOrdenados = (itensSessao + itensPost).sortedByDescending { it.timestamp }

        val filtrados = if (cursor != null) {
            todosOrdenados.filter { it.timestamp < cursor }
        } else {
            todosOrdenados
        }

        val pagina = filtrados.take(limit)
        val proximoCursor = if (pagina.size == limit) pagina.last().timestamp else null

        return FeedResponse(itens = pagina, proximoCursor = proximoCursor)
    }
}