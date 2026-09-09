package br.com.cibelly.scrobbledelia.repository

interface TopArtistaProjection {
    fun getArtista(): String
    fun getTotal(): Long
}