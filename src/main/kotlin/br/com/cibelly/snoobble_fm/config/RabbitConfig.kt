package br.com.cibelly.scrobbledelia.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    companion object {
        const val EXCHANGE_SCROBBLES = "scrobbles.exchange"
        const val FILA_SCROBBLE_NOVO = "scrobble.novo.queue"
        const val ROUTING_KEY_SCROBBLE_NOVO = "scrobble.novo"

        const val EXCHANGE_DLX = "scrobbles.dlx"
        const val FILA_SCROBBLE_NOVO_DLQ = "scrobble.novo.dlq"
        const val ROUTING_KEY_DLQ = "scrobble.novo.dlq"
    }

    @Bean
    fun scrobblesExchange(): DirectExchange {
        return DirectExchange(EXCHANGE_SCROBBLES)
    }

    @Bean
    fun filaScrobbleNovo(): Queue {
        return QueueBuilder.durable(FILA_SCROBBLE_NOVO)
            .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
            .withArgument("x-dead-letter-routing-key", ROUTING_KEY_DLQ)
            .build()
    }

    @Bean
    fun bindingScrobbleNovo(filaScrobbleNovo: Queue, scrobblesExchange: DirectExchange): Binding {
        return BindingBuilder.bind(filaScrobbleNovo).to(scrobblesExchange).with(ROUTING_KEY_SCROBBLE_NOVO)
    }

    @Bean
    fun dlxExchange(): DirectExchange {
        return DirectExchange(EXCHANGE_DLX)
    }

    @Bean
    fun filaDlq(): Queue {
        return Queue(FILA_SCROBBLE_NOVO_DLQ, true)
    }

    @Bean
    fun bindingDlq(filaDlq: Queue, dlxExchange: DirectExchange): Binding {
        return BindingBuilder.bind(filaDlq).to(dlxExchange).with(ROUTING_KEY_DLQ)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = Jackson2JsonMessageConverter()
        return template
    }

    @Bean
    fun rabbitListenerContainerFactory(
        connectionFactory: ConnectionFactory
    ): org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory {
        val factory = org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setMessageConverter(Jackson2JsonMessageConverter())
        factory.setDefaultRequeueRejected(false)
        return factory
    }
}