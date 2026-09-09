package br.com.cibelly.scrobbledelia.exception

class UsernameJaCadastradoException(username: String) : RuntimeException("Username já cadastrado: $username")