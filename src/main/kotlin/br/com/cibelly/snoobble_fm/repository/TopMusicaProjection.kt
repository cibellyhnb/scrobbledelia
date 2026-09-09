package br.com.cibelly.scrobbledelia.repository

interface TopMusicaProjection {
    fun getArtista(): String
    fun getMusica(): String
    fun getTotal(): Long
}