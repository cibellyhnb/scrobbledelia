package br.com.cibelly.scrobbledelia.service

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class LastFmRateLimiter(
    private val redisTemplate: StringRedisTemplate
) {

    private val limitePorSegundo = 4L

    fun aguardarSeNecessario() {
        while (true) {
            val segundoAtual = Instant.now().epochSecond
            val chave = "lastfm:chamadas:$segundoAtual"

            val contagem = redisTemplate.opsForValue().increment(chave)

            if (contagem == 1L) {
                redisTemplate.expire(chave, Duration.ofSeconds(2))
            }

            if (contagem != null && contagem <= limitePorSegundo) {
                return
            }

            Thread.sleep(100)
        }
    }
}