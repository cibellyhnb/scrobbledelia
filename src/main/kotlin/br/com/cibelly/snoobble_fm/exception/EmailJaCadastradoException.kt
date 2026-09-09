package br.com.cibelly.scrobbledelia.exception

class EmailJaCadastradoException(email: String) : RuntimeException("Email já cadastrado: $email")