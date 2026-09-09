package br.com.cibelly.scrobbledelia.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig {

    @Bean
    fun lastFmRestClient(builder: RestClient.Builder): RestClient {
        return builder
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .defaultHeader("User-Agent", "ScroobledeliaM/1.0 (contato: cibellyhnb@gmail.com)")
            .build()
    }

    @Bean
    fun ticketmasterRestClient(builder: RestClient.Builder): RestClient {
        return builder
            .baseUrl("https://app.ticketmaster.com/discovery/v2/")
            .build()
    }
}