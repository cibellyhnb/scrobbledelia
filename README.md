# Scrobbledelia 🎵

Rede social de música construída em cima do histórico de escuta do Last.fm. Compartilhe o que você está ouvindo, veja o que seus amigos andam escutando, descubra shows próximos, e receba um "Wrapped" pessoal com suas estatísticas de escuta.

## O que o projeto faz

- **Integração com Last.fm**: conecte sua conta e sincronize automaticamente seu histórico de escuta
- **Feed social**: veja sessões de escuta e posts de quem você segue, ordenados por tempo
- **Destaques automáticos**: o sistema identifica momentos notáveis (repetição obsessiva, escuta de madrugada, artista raro) sem você precisar fazer nada
- **Posts, curtidas e comentários**: compartilhe uma música específica com um comentário
- **Notificações**: seja avisado sobre shows de artistas que você ouve, perto de você
- **Estatísticas e Wrapped**: top artistas, top músicas, e um resumo consolidado do seu período de escuta

## Stack técnica

- **Kotlin** + **Spring Boot 4**
- **PostgreSQL** — banco de dados principal, com migrations via **Flyway**
- **Redis** — cache e controle de rate limit
- **RabbitMQ** — mensageria assíncrona (eventos de scrobble → notificações)
- **JWT** — autenticação stateless
- **Docker** + **Docker Compose** — toda a stack containerizada
- **Testes**: JUnit + MockK (unitários) e Testcontainers (integração)
- **Documentação de API**: Swagger/OpenAPI

## 🌐 Aplicação em produção

A API está no ar em: **https://scrobbledelia.onrender.com**

Documentação interativa (Swagger): **https://scrobbledelia.onrender.com/swagger-ui.html**

> Nota: a instância gratuita "dorme" após 15 minutos de inatividade. A primeira requisição depois disso pode levar 30-50 segundos para responder (é o container "acordando"), enquanto requisições seguintes são rápidas normalmente.

## Como rodar localmente

### Pré-requisitos
- Docker e Docker Compose
- Uma API key do [Last.fm](https://www.last.fm/api/account/create)
- Uma API key do [Ticketmaster Discovery API](https://developer.ticketmaster.com/)

### Passos

1. Clone o repositório
2. Crie um arquivo `.env` na raiz, seguindo o `.env.example`
3. Suba tudo com um comando:
```bash
   docker compose up -d --build
```
4. A API estará disponível em `http://localhost:8082
5. Documentação interativa em `http://localhost:8082/swagger-ui.html`

## Principais endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/usuarios` | Cadastro |
| POST | `/usuarios/login` | Login (retorna JWT) |
| PUT | `/usuarios/me/lastfm` | Conectar conta do Last.fm |
| POST | `/usuarios/me/scrobbles/sincronizar` | Sincronizar histórico manualmente |
| GET | `/feed` | Feed de quem você segue |
| POST | `/posts` | Compartilhar uma música |
| GET | `/estatisticas/me/wrapped` | Resumo consolidado de estatísticas |
| GET | `/notificacoes` | Notificações do usuário |

## Arquitetura

O projeto evoluiu em fases incrementais, cada uma introduzindo um conceito novo de backend:

1. Núcleo social (autenticação JWT, seguir/seguidores)
2. Integração com API externa (Last.fm, paginação, rate limiting)
3. Sincronização automática (job agendado + Redis)
4. Feed com paginação cursor-based
5. Posts sociais + sistema de destaques automáticos + agrupamento de sessões
6. Arquitetura orientada a eventos (RabbitMQ + dead letter queue)
7. Relatórios e estatísticas (com cache Redis)
8. Containerização completa e deploy

## Autor

Desenvolvido por Cibelly Batista como projeto de portfólio.