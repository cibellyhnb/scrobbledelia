package br.com.cibelly.scrobbledelia.repository

interface HoraProjection {
    fun getHora(): Int
    fun getTotal(): Long
}