package br.com.cibelly.scrobbledelia.dto

data class SessaoFeedData(
    val quantidade: Int,
    val artistasUnicos: List<String>,
    val destaque: Boolean,
    val motivoDestaque: String?
)