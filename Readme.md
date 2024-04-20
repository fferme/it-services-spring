# Projeto CRUD Spring Boot

## Sobre o Projeto:

Uma API cujo objetivo é facilitar o gerenciamento de clientes, ordens de serviço e itens de ordem de serviço.
Feito para facilitar o dia a dia de controle de um profissional técnico de informática.

## Tecnologias Utilizadas

- Java
- Spring Boot
- JPA / Hibernate
- Swagger
- Maven
- Docker
- MySQL
- Azure Cloud

## Endpoints relevantes

### Entidade Cliente

| Método | Endpoint                   | Descrição                       |
|--------|----------------------------|---------------------------------|
| GET    | `/api/clients/{id}`        | Recupera um cliente pelo ID.    |
| PUT    | `/api/clients/{id}`        | Atualiza um cliente existente.  |
| DELETE | `/api/clients/{id}`        | Deleta um cliente.              |
| GET    | `/api/clients`             | Recupera a lista de clientes.   |
| POST   | `/api/clients`             | Salva um novo cliente.          |
| DELETE | `/api/clients`             | Deleta todos os clientes.       |
| POST   | `/api/clients/import`      | Importa clientes de um arquivo. |
| GET    | `/api/clients/name/{name}` | Recupera um cliente pelo nome.  |

### Entidade Pedido

| Método | Endpoint           | Descrição                     |
|--------|--------------------|-------------------------------|
| GET    | `/api/orders/{id}` | Recupera um pedido pelo ID.   |
| PUT    | `/api/orders/{id}` | Atualiza um pedido existente. |
| DELETE | `/api/orders/{id}` | Deleta um pedido.             |
| GET    | `/api/orders`      | Recupera a lista de pedidos.  |
| POST   | `/api/orders`      | Salva um novo pedido.         |
| DELETE | `/api/orders`      | Deleta todos os pedidos.      |

### Entidade Item de Pedido

| Método | Endpoint                 | Descrição                              |
|--------|--------------------------|----------------------------------------|
| GET    | `/api/orderItems/{id}`   | Recupera um item de pedido pelo ID.    |
| PUT    | `/api/orderItems/{id}`   | Atualiza um item de pedido existente.  |
| DELETE | `/api/orderItems/{id}`   | Deleta um item de pedido.              |
| GET    | `/api/orderItems`        | Recupera a lista de itens de pedido.   |
| POST   | `/api/orderItems`        | Salva um novo item de pedido.          |
| DELETE | `/api/orderItems`        | Deleta todos os itens de pedido.       |
| POST   | `/api/orderItems/import` | Importa itens de pedido de um arquivo. |