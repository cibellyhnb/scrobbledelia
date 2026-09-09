package br.com.cibelly.scrobbledelia.dto

data class WrappedResponse(
    val periodo: PeriodoResponse,
    val resumo: EstatisticasGeraisResponse,
    val topArtistas: List<TopArtistaResponse>,
    val topMusicas: List<TopMusicaResponse>,
    val destaques: List<ScrobbleDestaqueResponse>
)

data class PeriodoResponse(
    val from: String?,
    val to: String?
)

data class ScrobbleDestaqueResponse(
    val musica: String,
    val artista: String,
    val motivoDestaque: String?,
    val tocadoEm: String
)