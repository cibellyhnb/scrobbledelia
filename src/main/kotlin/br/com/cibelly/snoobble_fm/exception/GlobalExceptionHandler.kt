package br.com.cibelly.scrobbledelia.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant
import br.com.cibelly.scrobbledelia.exception.LastFmIndisponivelException
import br.com.cibelly.scrobbledelia.exception.LastFmRateLimitException
import br.com.cibelly.scrobbledelia.exception.LastFmUsuarioInvalidoException
import br.com.cibelly.scrobbledelia.exception.LastFmNaoConectadoException

data class ErroResponse(
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val erro: String,
    val mensagem: String,
    val detalhes: List<String>? = null
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException::class, UsernameJaCadastradoException::class)
    fun handleConflito(ex: RuntimeException): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.CONFLICT, ex.message ?: "Conflito")
    }

    @ExceptionHandler(CredenciaisInvalidasException::class)
    fun handleCredenciaisInvalidas(ex: CredenciaisInvalidasException): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.UNAUTHORIZED, ex.message ?: "Credenciais inválidas")
    }

    @ExceptionHandler(UsuarioNaoEncontradoException::class)
    fun handleNaoEncontrado(ex: UsuarioNaoEncontradoException): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.NOT_FOUND, ex.message ?: "Não encontrado")
    }

    @ExceptionHandler(JaSeguindoException::class)
    fun handleJaSeguindo(ex: JaSeguindoException): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.CONFLICT, ex.message ?: "Conflito")
    }

    @ExceptionHandler(SeguirASiMesmoException::class)
    fun handleSeguirASiMesmo(ex: SeguirASiMesmoException): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.BAD_REQUEST, ex.message ?: "Requisição inválida")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidacao(ex: MethodArgumentNotValidException): ResponseEntity<ErroResponse> {
        val detalhes = ex.bindingResult.fieldErrors.map { erro: FieldError ->
            "${erro.field}: ${erro.defaultMessage}"
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErroResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                erro = "Dados inválidos",
                mensagem = "Um ou mais campos estão inválidos",
                detalhes = detalhes
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenerico(ex: Exception): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor")
    }

    private fun responder(status: HttpStatus, mensagem: String): ResponseEntity<ErroResponse> {
        return ResponseEntity.status(status).body(
            ErroResponse(
                status = status.value(),
                erro = status.reasonPhrase,
                mensagem = mensagem
            )
        )
    }

    @ExceptionHandler(LastFmUsuarioInvalidoException::class)
    fun handleLastFmUsuarioInvalido(ex: LastFmUsuarioInvalidoException): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.NOT_FOUND, ex.message ?: "Usuário não encontrado no Last.fm")
    }

    @ExceptionHandler(LastFmRateLimitException::class)
    fun handleLastFmRateLimit(ex: LastFmRateLimitException): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.TOO_MANY_REQUESTS, ex.message ?: "Limite excedido")
    }

    @ExceptionHandler(LastFmIndisponivelException::class)
    fun handleLastFmIndisponivel(ex: LastFmIndisponivelException): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.SERVICE_UNAVAILABLE, ex.message ?: "Last.fm indisponível")
    }

    @ExceptionHandler(LastFmNaoConectadoException::class)
    fun handleLastFmNaoConectado(ex: LastFmNaoConectadoException): ResponseEntity<ErroResponse> {
        return responder(HttpStatus.BAD_REQUEST, ex.message ?: "Conecte sua conta do Last.fm")
    }
}