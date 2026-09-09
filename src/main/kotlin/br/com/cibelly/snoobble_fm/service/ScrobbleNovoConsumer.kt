package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.config.RabbitConfig
import br.com.cibelly.scrobbledelia.dto.ScrobbleNovoMensagem
import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ScrobbleNovoConsumer(
    private val usuarioRepository: UsuarioRepository,
    private val ticketmasterClient: TicketmasterClient,
    private val notificacaoService: NotificacaoService
) {

    private val logger = LoggerFactory.getLogger(ScrobbleNovoConsumer::class.java)

    @RabbitListener(queues = [RabbitConfig.FILA_SCROBBLE_NOVO])
    fun processar(mensagem: ScrobbleNovoMensagem) {
      
        logger.info("Mensagem recebida: usuário ${mensagem.usuarioId} ouviu '${mensagem.musica}' de ${mensagem.artista}")

        val usuario = usuarioRepository.findById(mensagem.usuarioId).orElse(null) ?: return
        val cidade = usuario.cidade ?: return

        val evento = ticketmasterClient.buscarPrimeiroShow(mensagem.artista, cidade) ?: return

        val local = evento.embedded?.venues?.firstOrNull()?.name ?: cidade

        val referencia = "${mensagem.artista}|$local|${evento.dates.start.localDate}"

        notificacaoService.criar(
            usuario = usuario,
            tipo = "SHOW_PROXIMO",
            titulo = "Show de ${mensagem.artista} perto de você!",
            mensagem = "${mensagem.artista} vai se apresentar em $local em ${evento.dates.start.localDate}",
            referenciaExterna = referencia
        )

        logger.info("Notificação de show criada para usuário ${usuario.username}")
    }


}