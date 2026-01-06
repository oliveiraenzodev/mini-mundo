🚀 Mini Mundo — Projeto de Laboratório para Avaliação Técnica

📌 Visão Geral

O Mini Mundo é um projeto de laboratório criado para avaliação técnica de desenvolvedores,
simulando um ambiente real de trabalho com sistemas legados Java, versionamento estruturado,
containerização e automação de build.

O objetivo não é apenas entregar funcionalidades, mas avaliar boas práticas de engenharia,
capacidade de adaptação tecnológica e domínio de ferramentas modernas aplicadas a sistemas legados.


🎯 Objetivo da Avaliação

Este projeto avalia a capacidade do candidato em:

- Implementar funcionalidades seguindo requisitos técnicos claros
- Trabalhar com sistemas legados Java (JSF / Hibernate)
- Utilizar Gitflow e Conventional Commits
- Criar e executar ambientes Dockerizados
- Publicar imagens no Docker Hub
- Implementar CI/CD baseado em versionamento por TAGs


🛠️ Tecnologias Utilizadas

📦 Stack Original do Enunciado

Java: 1.6 ou 1.8
Hibernate: 4.x
PostgreSQL: 8.3
PrimeFaces: 3.x
JBoss / WildFly: 7.0.2


⚠️ Divergências Técnicas (Justificadas)

Algumas versões do enunciado não possuem mais suporte prático ou imagens Docker confiáveis.
Para garantir viabilidade técnica, foram utilizadas versões compatíveis e imediatamente superiores.

PostgreSQL:
- Enunciado: 8.3
- Utilizado: 9.x
- Motivo: versão obsoleta sem imagem Docker oficial

WildFly:
- Enunciado: 7.0.2
- Utilizado: 8.2.1.Final
- Motivo: ausência de imagem oficial estável

PrimeFaces:
- Enunciado: 3.x
- Utilizado: 4.0
- Motivo: compatível com JSF 2.1 e Hibernate 4

IMPORTANTE:
Essas mudanças não alteram o escopo funcional, apenas garantem estabilidade,
segurança e reprodutibilidade do ambiente.


📂 Organização do Desenvolvimento

🔀 Gitflow

O projeto segue o modelo Gitflow:

- master   → código estável / produção
- dev  → integração contínua
- feat/*   → implementação de funcionalidades
- fix/*    → correções emergenciais

Cada funcionalidade deve ser desenvolvida em uma branch própria,
associada a uma Issue.


📝 Conventional Commits

Todos os commits seguem o padrão Conventional Commits.

Formato:
<tipo>: <descrição>

Exemplos:
feat: implementa tela de login
fix: corrige validação de senha
chore: ajusta configuração docker
docs: atualiza README

Tipos utilizados:
- feat
- fix
- docs
- chore
- refactor
- test


🧩 Escopo Funcional (Issues)

Cada item abaixo deve ser tratado como uma Issue independente:

- Implementação da tela de login
- CRUD de Projetos
- CRUD de Tarefas (associadas a Projetos)

Cada Issue possui um arquivo .md com instruções específicas.


🐳 Docker — Ambiente de Desenvolvimento

O projeto foi pensado para que nenhuma ferramenta precise ser instalada localmente,
além do Docker.

📋 Pré-requisitos

- Docker
- Docker Compose


▶️ Subindo o ambiente (DEV)

docker compose up --build


🔗 Serviços disponíveis

Aplicação:        http://localhost:8080
Admin WildFly:    http://localhost:9990
PostgreSQL:       porta 5432


📦 Arquitetura Docker

app:
- WildFly 8.2.1.Final
- Deploy automático do .war
- Datasource e driver configurados em build time (standalone.xml)
- Nenhuma execução de CLI em runtime

db:
- PostgreSQL 9.x
- Volume persistente


🔌 Configuração do Banco de Dados

A configuração do banco é feita em build time diretamente no standalone.xml:

- Driver JDBC PostgreSQL
- Datasource MiniMundoDS
- JNDI: java:/jdbc/MiniMundoDS

Variáveis de ambiente disponíveis:
- DB_HOST
- DB_PORT
- DB_NAME
- DB_USER
- DB_PASSWORD

IMPORTANTE:
Não há execução de init.cli em runtime, garantindo:
- startup mais rápido
- ausência de erro de driver duplicado
- comportamento idêntico entre dev e release


🔁 CI/CD — Build e Publicação de Imagem

📌 Gatilho da Pipeline

A pipeline é executada automaticamente quando uma TAG é criada na branch master,
seguindo o regex:

/^(v|V)?(\d+\.)?(\d+\.)?(\*|\d+)\.?(hf\d+|Hf\d+|HF\d+)?$/

Exemplos válidos:
- 1.0.0
- v1.2.0
- 2.0.1-hf1


📦 Ações da Pipeline

- Build do projeto (Maven)
- Build da imagem Docker
- Push automático para o Docker Hub
- Publicação das tags:
  - TAG da versão
  - latest

A pipeline pode ser implementada via Jenkins, GitHub Actions ou GitLab CI.


🐙 Docker Hub

A imagem publicada permite:
- ajuste de porta
- ajuste de banco de dados
- execução local simples

Exemplo:

docker run -p 8080:8080 oliveiraenzodev/mini-mundo:1.0.0


👩‍💻 Novo Desenvolvedor — Primeiros Passos

1. Clone o repositório
2. Execute:
   docker compose up --build
3. Acesse:
   http://localhost:8080

Nenhuma instalação adicional é necessária.


✅ Conclusão

Este projeto simula um ambiente real de desenvolvimento, equilibrando:

- tecnologias legadas
- boas práticas modernas
- automação
- infraestrutura como código

Todas as divergências técnicas foram documentadas, justificadas e controladas,
mantendo aderência ao espírito do enunciado e garantindo viabilidade prática.
