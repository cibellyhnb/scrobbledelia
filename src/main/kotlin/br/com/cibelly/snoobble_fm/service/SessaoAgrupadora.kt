package br.com.cibelly.scrobbledelia.service

import br.com.cibelly.scrobbledelia.entity.Scrobble
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class SessaoAgrupadora {

    private val intervaloMaximoMinutos = 30L

    fun agrupar(scrobbles: List<Scrobble>): List<SessaoEscuta> {
        val porUsuario = scrobbles.groupBy { it.usuario.id }

        val todasSessoes = mutableListOf<SessaoEscuta>()

        for ((_, scrobblesDoUsuario) in porUsuario) {
            val ordenados = scrobblesDoUsuario.sortedBy { it.tocadoEm }
            var sessaoAtual = mutableListOf<Scrobble>()

            for (scrobble in ordenados) {
                if (sessaoAtual.isEmpty()) {
                    sessaoAtual.add(scrobble)
                    continue
                }

                val minutosDesdeUltimo = Duration.between(sessaoAtual.last().tocadoEm, scrobble.tocadoEm).toMinutes()

                if (minutosDesdeUltimo <= intervaloMaximoMinutos) {
                    sessaoAtual.add(scrobble)
                } else {
                    todasSessoes.add(SessaoEscuta(usuario = sessaoAtual.first().usuario, scrobbles = sessaoAtual.toList()))
                    sessaoAtual = mutableListOf(scrobble)
                }
            }

            if (sessaoAtual.isNotEmpty()) {
                todasSessoes.add(SessaoEscuta(usuario = sessaoAtual.first().usuario, scrobbles = sessaoAtual.toList()))
            }
        }

        return todasSessoes.sortedByDescending { it.fim }
    }
}