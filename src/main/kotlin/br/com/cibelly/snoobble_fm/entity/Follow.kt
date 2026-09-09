package br.com.cibelly.scrobbledelia.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "follows")
class Follow(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguidor_id", nullable = false)
    var seguidor: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguido_id", nullable = false)
    var seguido: Usuario,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "criado_em", nullable = false, updatable = false)
    val criadoEm: LocalDateTime = LocalDateTime.now()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Follow) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}