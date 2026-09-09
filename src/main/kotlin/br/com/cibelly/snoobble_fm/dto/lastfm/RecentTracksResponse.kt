package br.com.cibelly.scrobbledelia.dto.lastfm

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonFormat

data class RecentTracksResponse(
    val recenttracks: RecentTracksWrapper
)

data class RecentTracksWrapper(
    @field:JsonFormat(with = [JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY])
    val track: List<TrackDto>,
    @field:JsonProperty("@attr")
    val attr: RecentTracksAttr
)

data class TrackDto(
    val name: String,
    val artist: TextWrapper,
    val album: TextWrapper,
    val date: DateDto? = null,
    @field:JsonProperty("@attr")
    val attr: TrackAttr? = null
)

data class TextWrapper(
    @field:JsonProperty("#text")
    val text: String
)

data class DateDto(
    val uts: String,
    @field:JsonProperty("#text")
    val text: String
)

data class TrackAttr(
    val nowplaying: String? = null
)

data class RecentTracksAttr(
    val user: String,
    val page: String,
    val perPage: String,
    val totalPages: String,
    val total: String
)