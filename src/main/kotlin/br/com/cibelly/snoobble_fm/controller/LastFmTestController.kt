package br.com.cibelly.scrobbledelia.controller

import br.com.cibelly.scrobbledelia.dto.lastfm.RecentTracksResponse
import br.com.cibelly.scrobbledelia.service.LastFmClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import br.com.cibelly.scrobbledelia.dto.lastfm.TrackDto

@RestController
class LastFmTestController(
    private val lastFmClient: LastFmClient
) {

    @GetMapping("/lastfm/teste/{username}")
    fun testar(@PathVariable username: String): RecentTracksResponse {
        return lastFmClient.buscarScrobbles(username)
    }

    @GetMapping("/lastfm/teste/{username}/todos")
    fun testarTodos(@PathVariable username: String): List<TrackDto> {
        return lastFmClient.buscarTodosScrobbles(username)
    }
}