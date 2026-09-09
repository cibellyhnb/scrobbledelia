package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.dto.ticketmaster.EventDto
import br.com.cibelly.scrobbledelia.dto.ticketmaster.TicketmasterEventsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class TicketmasterClient(
    private val ticketmasterRestClient: RestClient,
    @Value("\${ticketmaster.api-key}") private val apiKey: String
) {

    fun buscarPrimeiroShow(artista: String, cidade: String): EventDto? {
        val resposta = ticketmasterRestClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("events.json")
                    .queryParam("keyword", artista)
                    .queryParam("city", cidade)
                    .queryParam("apikey", apiKey)
                    .build()
            }
            .retrieve()
            .body(TicketmasterEventsResponse::class.java)

        return resposta?.embedded?.events?.firstOrNull()
    }
}