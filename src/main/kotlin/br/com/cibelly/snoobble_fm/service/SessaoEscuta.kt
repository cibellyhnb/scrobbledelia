package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.entity.Scrobble
import br.com.cibelly.scrobbledelia.entity.Usuario
import java.time.LocalDateTime

data class SessaoEscuta(
    val usuario: Usuario,
    val scrobbles: List<Scrobble>
) {
    val inicio: LocalDateTime get() = scrobbles.minOf { it.tocadoEm }
    val fim: LocalDateTime get() = scrobbles.maxOf { it.tocadoEm }
    val quantidade: Int get() = scrobbles.size
    val scrobbleDestaque: Scrobble? get() = scrobbles.firstOrNull { it.destaque }
    val artistasUnicos: List<String> get() = scrobbles.map { it.artista }.distinct()
}