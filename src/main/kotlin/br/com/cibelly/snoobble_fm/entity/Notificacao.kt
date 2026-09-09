package br.com.cibelly.scrobbledelia.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "notificacoes")
class Notificacao(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    var usuario: Usuario,

    @Column(nullable = false, length = 50)
    var tipo: String,

    @Column(nullable = false, length = 200)
    var titulo: String,

    @Column(nullable = false, length = 500)
    var mensagem: String,

    @Column(nullable = false)
    var lida: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "criado_em", nullable = false, updatable = false)
    val criadoEm: LocalDateTime = LocalDateTime.now(),

    @Column(name = "referencia_externa", length = 200)
    var referenciaExterna: String? = null,

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Notificacao) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}