package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.dto.lastfm.RecentTracksResponse
import br.com.cibelly.scrobbledelia.dto.lastfm.TrackDto
import br.com.cibelly.scrobbledelia.exception.LastFmIndisponivelException
import br.com.cibelly.scrobbledelia.exception.LastFmRateLimitException
import br.com.cibelly.scrobbledelia.exception.LastFmUsuarioInvalidoException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient
import tools.jackson.databind.json.JsonMapper

@Service
class LastFmClient(
    private val lastFmRestClient: RestClient,
    private val jsonMapper: JsonMapper,
    private val rateLimiter: LastFmRateLimiter,
    @Value("\${lastfm.api-key}") private val apiKey: String
) {

    fun buscarScrobbles(username: String, page: Int = 1, limit: Int = 200, from: Long? = null): RecentTracksResponse {
        val corpo = try {
            lastFmRestClient.get()
                .uri { uriBuilder ->
                    val builder = uriBuilder
                        .queryParam("method", "user.getrecenttracks")
                        .queryParam("user", username)
                        .queryParam("api_key", apiKey)
                        .queryParam("format", "json")
                        .queryParam("page", page)
                        .queryParam("limit", limit)

                    if (from != null) {
                        builder.queryParam("from", from)
                    }

                    builder.build()
                }
                .retrieve()
                .body(String::class.java)
        } catch (ex: HttpClientErrorException) {
            when (ex.statusCode.value()) {
                404 -> throw LastFmUsuarioInvalidoException(username)
                429 -> throw LastFmRateLimitException()
                else -> throw LastFmIndisponivelException("Erro ao chamar Last.fm: ${ex.statusCode}")
            }
        } catch (ex: HttpServerErrorException) {
            throw LastFmIndisponivelException()
        } catch (ex: ResourceAccessException) {
            throw LastFmIndisponivelException("Não foi possível conectar ao Last.fm (timeout ou rede)")
        } ?: throw LastFmIndisponivelException()

        val node = jsonMapper.readTree(corpo)

        if (node.has("error")) {
            val codigoErro = node.get("error").asInt()
            when (codigoErro) {
                6 -> throw LastFmUsuarioInvalidoException(username)
                29 -> throw LastFmRateLimitException()
                else -> throw LastFmIndisponivelException(
                    node.get("message")?.asString() ?: "Erro desconhecido do Last.fm"
                )
            }
        }

        return jsonMapper.readValue(corpo, RecentTracksResponse::class.java)
    }

    fun buscarTodosScrobbles(username: String, maxPaginas: Int = 5, from: Long? = null): List<TrackDto> {
        val todos = mutableListOf<TrackDto>()

        val primeiraPagina = buscarScrobbles(username, page = 1, from = from)
        todos.addAll(primeiraPagina.recenttracks.track)

        val totalPaginas = primeiraPagina.recenttracks.attr.totalPages.toInt()
        val paginasParaBuscar = minOf(totalPaginas, maxPaginas)

        for (pagina in 2..paginasParaBuscar) {
            val resposta = buscarScrobbles(username, page = pagina, from = from)
            todos.addAll(resposta.recenttracks.track)
        }

        return todos
    }
}