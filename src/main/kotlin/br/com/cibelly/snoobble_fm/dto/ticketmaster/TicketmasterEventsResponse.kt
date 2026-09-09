package br.com.cibelly.scrobbledelia.dto.ticketmaster

import com.fasterxml.jackson.annotation.JsonProperty

data class TicketmasterEventsResponse(
    @field:JsonProperty("_embedded")
    val embedded: EmbeddedEvents? = null
)

data class EmbeddedEvents(
    val events: List<EventDto>
)

data class EventDto(
    val name: String,
    val dates: DatesDto,
    @field:JsonProperty("_embedded")
    val embedded: EmbeddedVenues? = null
)

data class DatesDto(
    val start: StartDto
)

data class StartDto(
    val localDate: String
)

data class EmbeddedVenues(
    val venues: List<VenueDto>
)

data class VenueDto(
    val name: String,
    val city: CityDto
)

data class CityDto(
    val name: String
)