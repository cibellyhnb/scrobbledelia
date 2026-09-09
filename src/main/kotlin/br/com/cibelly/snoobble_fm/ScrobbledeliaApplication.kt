package br.com.cibelly.scrobbledelia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ScrobbledeliaApplication

fun main(args: Array<String>) {
    runApplication<ScrobbledeliaApplication>(*args)
}