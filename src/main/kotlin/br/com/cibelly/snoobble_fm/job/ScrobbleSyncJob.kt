package br.com.cibelly.scrobbledelia.job

import br.com.cibelly.scrobbledelia.repository.UsuarioRepository
import br.com.cibelly.scrobbledelia.service.ScrobbleService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScrobbleSyncJob(
    private val usuarioRepository: UsuarioRepository,
    private val scrobbleService: ScrobbleService
) {

    private val logger = LoggerFactory.getLogger(ScrobbleSyncJob::class.java)

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    fun sincronizarTodos() {
        val usuariosConectados = usuarioRepository.findByLastfmUsernameIsNotNull()

        logger.info("Iniciando sincronização automática para ${usuariosConectados.size} usuário(s)")

        var sucessos = 0
        var falhas = 0

        for (usuario in usuariosConectados) {
            try {
                val salvos = scrobbleService.sincronizar(usuario.id!!)
                logger.info("Usuário ${usuario.username}: $salvos scrobble(s) novo(s)")
                sucessos++
            } catch (ex: Exception) {
                logger.error("Falha ao sincronizar usuário ${usuario.username}: ${ex.message}")
                falhas++
            }
        }

        logger.info("Sincronização automática finalizada: $sucessos sucesso(s), $falhas falha(s)")
    }
}