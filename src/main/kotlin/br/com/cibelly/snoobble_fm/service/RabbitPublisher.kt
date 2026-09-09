package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.config.RabbitConfig
import br.com.cibelly.scrobbledelia.dto.ScrobbleNovoMensagem
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitPublisher(
    private val rabbitTemplate: RabbitTemplate
) {

    fun publicarScrobbleNovo(mensagem: ScrobbleNovoMensagem) {
        rabbitTemplate.convertAndSend(
            RabbitConfig.EXCHANGE_SCROBBLES,
            RabbitConfig.ROUTING_KEY_SCROBBLE_NOVO,
            mensagem
        )
    }
}