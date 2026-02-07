# 🎟️ Event Management Backend

Backend **Java + Spring Boot** para um **sistema de gestão de eventos**, com foco em **segurança, regras de negócio no domínio e integração real com pagamentos**.

O sistema permite que usuários se inscrevam em eventos, realizem pagamentos, recebam QR Code de participação e efetuem **check-in presencial validado pelo backend**.

---

## 🎯 Objetivo do Projeto

Demonstrar a construção de um **backend profissional**, aplicando:

* Arquitetura em camadas
* Domínio forte (regras encapsuladas nas entidades)
* Autenticação e autorização com **JWT**
* Integração real com **Mercado Pago**
* Processamento de **webhooks**
* Geração e validação de **QR Code**
* Check-in presencial seguro
* Comunicação assíncrona (SMS)

---

## 🧠 Visão Geral da Arquitetura

O backend é uma **API REST stateless**, responsável por:

* Autenticar usuários
* Controlar permissões por perfil (ADMIN / USER)
* Gerenciar eventos e inscrições
* Processar pagamentos de forma segura
* Confirmar pagamentos apenas via webhook
* Gerar QR Code após pagamento
* Validar check-in presencial
* Enviar notificações por SMS

📌 **O frontend nunca confirma pagamento nem executa regras críticas.**

---

## 🔐 Segurança

### Autenticação

* Login com email e senha
* Senhas protegidas com **BCrypt**
* Geração de **JWT assinado (HMAC SHA256)**

### Autorização

* Controle por roles (`ADMIN`, `USER`)
* Proteção de endpoints via **Spring Security**
* Segurança em nível de método com `@PreAuthorize`

### Modelo

* API totalmente **stateless**
* Nenhuma sessão em servidor
* JWT validado a cada requisição protegida

---

## 🧩 Domínio

### User

* Representa usuários do sistema
* Tipos: `ADMIN`, `USER`
* Usado para controle de permissões

---

### Event

* Representa um evento
* Status: `ACTIVE`, `CLOSED`
* Apenas eventos ativos aceitam inscrições e check-in

---

### Registration (Entidade Central)

Representa a inscrição de um usuário em um evento.

**Restrições**

* Um usuário só pode se inscrever uma vez por evento

**Estados**

* `PaymentStatus`: `PENDING`, `PAID`
* `CheckInStatus`: `NOT_COMPLETED`, `COMPLETED`

**Estado inicial**

```
PENDING + NOT_COMPLETED
```

**Regras de domínio**

* `confirmPayment()`
* `isPaid()`
* `canCheckIn()`
* `performCheckIn()`

📌 Todas as transições de estado acontecem **dentro da entidade**, não no controller.

---

## ⚙️ Camada de Serviços

### RegistrationService

Responsável por **todas as regras de negócio relacionadas à inscrição**.

Principais operações:

* Registrar participação em evento
* Confirmar pagamento (uso exclusivo do webhook)
* Gerar QR Code de participação
* Validar e executar check-in presencial

---

## 💳 Integração com Pagamento (Mercado Pago)

### Características

* SDK oficial do Mercado Pago
* Checkout Pro
* Pagamento confirmado **exclusivamente via webhook**
* Backend como **fonte única da verdade**

### Fluxo de Pagamento

1. Usuário solicita pagamento
2. Backend cria preferência no Mercado Pago
3. Usuário paga no checkout
4. Mercado Pago envia webhook
5. Backend valida o pagamento real
6. Inscrição é confirmada como `PAID`

📌 O frontend **nunca** confirma pagamento.

---

## 🔔 Webhook

* Endpoint público
* Validação da notificação
* Consulta do pagamento real na API do Mercado Pago
* Confirmação da inscrição apenas se o status for `approved`
* Processo transacional e idempotente

---

## 🔳 QR Code & Check-in

### QR Code

* Gerado apenas após pagamento confirmado
* Formato:

```
participacao:{registrationId}
```

### Check-in Presencial

* Executado por usuário `ADMIN`
* QR Code é lido por uma interface web simples
* Backend valida:

    * existência da inscrição
    * pagamento confirmado
    * evento ativo
    * check-in ainda não realizado

---

## 📩 Integração com SMS (Twilio)

* Envio de SMS após confirmação de pagamento
* Falha no envio **não invalida o pagamento**
* Exceções são tratadas e logadas

---

## 🧪 Tratamento de Erros

O projeto utiliza exceções específicas para regras de negócio, como:

* Evento inativo
* Inscrição inexistente
* Pagamento já confirmado
* QR Code inválido
* Tentativa de check-in inválida

Isso garante:

* clareza na API
* respostas consistentes
* melhor manutenção

---

## 🛠️ Tecnologias Utilizadas

* Java 21
* Spring Boot 3.x
* Spring Security (OAuth2 Resource Server)
* JWT
* Spring Data JPA
* Mercado Pago SDK
* ZXing (QR Code)
* Twilio
* MySQL / PostgreSQL
* Swagger / OpenAPI

---

## 🚀 Status do Projeto

* ✅ Autenticação e autorização
* ✅ Pagamentos integrados
* ✅ Webhook funcional
* ✅ QR Code gerado após pagamento
* ✅ Check-in presencial validado
* ✅ SMS integrado (limitação esperada em conta trial)
* 🔄 Evoluções futuras planejadas

---

## 📌 Considerações Finais

Este projeto foi desenvolvido com foco em:

* **boas práticas de backend**
* **segurança**
* **responsabilidade de camadas**
* **regras de negócio bem definidas**
* **integração com serviços reais**

Ele serve como **projeto de portfólio**, **base para evolução** e **exemplo de arquitetura backend moderna**.

---
