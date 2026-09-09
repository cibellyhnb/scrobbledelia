package br.com.cibelly.scrobbledelia.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "curtidas")
class Curtida(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    var usuario: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "criado_em", nullable = false, updatable = false)
    val criadoEm: LocalDateTime = LocalDateTime.now()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Curtida) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}