package br.com.cibelly.scrobbledelia.repository

import br.com.cibelly.scrobbledelia.entity.Notificacao
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NotificacaoRepository : JpaRepository<Notificacao, UUID> {
    fun findByUsuarioIdOrderByCriadoEmDesc(usuarioId: UUID, pageable: Pageable): Page<Notificacao>
    fun countByUsuarioIdAndLidaFalse(usuarioId: UUID): Long

    fun existsByUsuarioIdAndTipoAndReferenciaExterna(
        usuarioId: UUID,
        tipo: String,
        referenciaExterna: String
    ): Boolean
}

