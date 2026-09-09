package br.com.cibelly.scrobbledelia.controller

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RedisTestController(
    private val redisTemplate: StringRedisTemplate
) {

    @GetMapping("/redis/teste")
    fun testar(): String {
        redisTemplate.opsForValue().set("chave-teste", "funcionou!")
        return redisTemplate.opsForValue().get("chave-teste") ?: "não encontrou"
    }
}