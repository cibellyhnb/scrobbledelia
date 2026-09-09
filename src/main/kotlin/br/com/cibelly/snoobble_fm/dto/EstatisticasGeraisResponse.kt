package br.com.cibelly.scrobbledelia.dto

data class EstatisticasGeraisResponse(
    val totalScrobbles: Long,
    val artistasUnicos: Long,
    val musicasUnicas: Long,
    val horaMaisAtiva: Int?
)