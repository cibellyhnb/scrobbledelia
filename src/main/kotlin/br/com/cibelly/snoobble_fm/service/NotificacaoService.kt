package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.dto.NotificacaoResponse
import br.com.cibelly.scrobbledelia.entity.Notificacao
import br.com.cibelly.scrobbledelia.entity.Usuario
import br.com.cibelly.scrobbledelia.exception.NotificacaoNaoEncontradaException
import br.com.cibelly.scrobbledelia.exception.SemPermissaoException
import br.com.cibelly.scrobbledelia.repository.NotificacaoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NotificacaoService(
    private val notificacaoRepository: NotificacaoRepository
) {

    fun criar(usuario: Usuario, tipo: String, titulo: String, mensagem: String): Notificacao {
        val notificacao = Notificacao(
            usuario = usuario,
            tipo = tipo,
            titulo = titulo,
            mensagem = mensagem
        )
        return notificacaoRepository.save(notificacao)
    }

    fun listar(usuarioId: UUID, pageable: Pageable): Page<NotificacaoResponse> {
        return notificacaoRepository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId, pageable)
            .map { it.paraResponse() }
    }

    fun marcarComoLida(usuarioId: UUID, notificacaoId: UUID) {
        val notificacao = notificacaoRepository.findById(notificacaoId)
            .orElseThrow { NotificacaoNaoEncontradaException(notificacaoId) }

        if (notificacao.usuario.id != usuarioId) {
            throw SemPermissaoException("Você só pode marcar suas próprias notificações")
        }

        notificacao.lida = true
        notificacaoRepository.save(notificacao)
    }

    fun contarNaoLidas(usuarioId: UUID): Long {
        return notificacaoRepository.countByUsuarioIdAndLidaFalse(usuarioId)
    }

    private fun Notificacao.paraResponse() = NotificacaoResponse(
        id = this.id!!,
        tipo = this.tipo,
        titulo = this.titulo,
        mensagem = this.mensagem,
        lida = this.lida,
        criadoEm = this.criadoEm
    )

    fun criar(
        usuario: Usuario,
        tipo: String,
        titulo: String,
        mensagem: String,
        referenciaExterna: String? = null
    ): Notificacao? {
        if (referenciaExterna != null) {
            val jaExiste = notificacaoRepository.existsByUsuarioIdAndTipoAndReferenciaExterna(
                usuario.id!!, tipo, referenciaExterna
            )
            if (jaExiste) return null
        }

        val notificacao = Notificacao(
            usuario = usuario,
            tipo = tipo,
            titulo = titulo,
            mensagem = mensagem,
            referenciaExterna = referenciaExterna
        )
        return notificacaoRepository.save(notificacao)
    }
}