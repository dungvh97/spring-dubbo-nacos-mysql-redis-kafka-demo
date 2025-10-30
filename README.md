---

## 🏗️ 1. Overall Project Overview

### 🧩 Architecture Summary

| Layer                         | Technology                                 | Purpose                                   |
| ----------------------------- | ------------------------------------------ | ----------------------------------------- |
| **Service Framework**         | Spring Boot 2.1.5                          | RESTful API, microservice foundation      |
| **Service Registry & Config** | Nacos                                      | Service discovery + config management     |
| **RPC Communication**         | Apache Dubbo 2.7.7 (light version)         | Inter-service RPC calls                   |
| **Database**                  | MySQL                                      | Persistent storage                        |
| **Cache**                     | Redis                                      | Caching, sessions, token store            |
| **Message Queue (optional)**  | Kafka                                      | Async event-driven communication          |
| **Build Tool**                | Maven 3.6.0+                               | Dependency management                     |
| **Plugin System**             | PF4J 3.0.1                                 | Optional extensibility for future plugins |
| **Lombok 1.18.30**            | Code simplification (getters/setters/logs) |                                           |

---

## 🧠 2. Service Design

You’ll build **3 main microservices** + optional supporting ones.

| Service                        | Port                           | Purpose                                            | Exposed Interface |
| ------------------------------ | ------------------------------ | -------------------------------------------------- | ----------------- |
| **User-Service**               | `8081` (HTTP), `26881` (Dubbo) | Manage users, login, registration                  | REST + Dubbo      |
| **Product-Service**            | `8082` (HTTP), `26882` (Dubbo) | Manage product catalog                             | REST + Dubbo      |
| **Order-Service**              | `8083` (HTTP), `26883` (Dubbo) | Handle orders, inventory check, payment simulation | REST + Dubbo      |
| **(Optional) Payment-Service** | `8084` (HTTP), `26884` (Dubbo) | Payment processing simulation                      | Dubbo only        |
| **(Optional) Gateway-Service** | `8080`                         | API gateway or aggregator                          | REST only         |

---

## 🗄️ 3. Database Design (MySQL)

Each service has its own database schema.

### Example

* **user_db**

  * `users(id, username, email, password_hash, created_at)`
* **product_db**

  * `products(id, name, price, stock, category, updated_at)`
* **order_db**

  * `orders(id, user_id, total_amount, status, created_at)`
  * `order_items(id, order_id, product_id, quantity, price)`

---

## 🔌 4. Service Communication

### REST API

* Public-facing endpoints (for web/mobile)
* Example: `GET /api/products`, `POST /api/orders`

### Dubbo RPC

* Internal microservice calls

  * `OrderService` calls `ProductService` via Dubbo (to check stock)
  * `OrderService` calls `UserService` (to verify user info)

Example Interface:

```java
public interface ProductDubboService {
    ProductDTO getProductById(Long id);
    boolean decreaseStock(Long id, int quantity);
}
```

---

## ⚙️ 5. Nacos Configuration

Each service registers with Nacos:

```yaml
spring:
  application:
    name: user-service
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
      config:
        server-addr: 127.0.0.1:8848
```

Run Nacos via Docker or manually:

```bash
docker run -d --name nacos -p 8848:8848 nacos/nacos-server:1.4.1
```

---

## 🧱 6. Redis Cache

* Cache user sessions, product lookups, and hot data.
* Example use:

  * `UserService`: store user tokens
  * `ProductService`: cache product list
  * `OrderService`: cache frequently accessed orders

---

## 📨 7. (Optional) Kafka Usage

Use Kafka for asynchronous communication:

* Order created → send "OrderCreated" message
* Payment service consumes and processes payment asynchronously

Example:

```java
Kafka:
  name-server: 127.0.0.1:9876
```

---

## 🧩 8. Directory Structure (Monorepo Style)

```
online-business/
│
├── pom.xml                            # Parent POM (dependency management, plugin management)
│
├── common/
│   ├── common-dubbo-api/              # Shared Dubbo interfaces + DTOs only
│   │   ├── src/main/java/com/demo/common/api/
│   │   │   ├── user/                  # RPC DTOs + interfaces for User
│   │   │   │   ├── UserDTO.java
│   │   │   │   └── UserFacade.java
│   │   │   ├── product/               # RPC DTOs + interfaces for Product
│   │   │   │   ├── ProductDTO.java
│   │   │   │   └── ProductFacade.java
│   │   │   └── common/
│   │   │       ├── ApiResponse.java
│   │   │       └── PageResult.java
│   │   └── pom.xml
│   │
│   ├── common-util/                   # Common utils (DateUtil, JwtUtil, etc.)
│   └── common-model/                  # Common enums/constants (e.g. ErrorCode, Roles)
│
├── user-service/
│   ├── src/main/java/com/demo/user/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   └── config/
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── bootstrap.yml (optional if using Nacos config)
│   ├── pom.xml
│
├── product-service/
│   ├── src/main/java/com/demo/product/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   └── config/
│   ├── src/main/resources/application.yml
│   └── pom.xml
│
├── order-service/
│   ├── src/main/java/com/demo/order/
│   ├── src/main/resources/application.yml
│   └── pom.xml
│
├── gateway-service/                   # Optional Spring Cloud Gateway or Dubbo Gateway
│
└── README.md

```

---

## ⚙️ 9. Maven Parent POM Example

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.demo</groupId>
  <artifactId>online-business</artifactId>
  <version>1.0.0</version>
  <packaging>pom</packaging>

  <modules>
    <module>common/common-model</module>
    <module>common/common-dubbo-api</module>
    <module>user-service</module>
    <module>product-service</module>
    <module>order-service</module>
  </modules>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>3.5.7</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
      <dependency>
        <groupId>org.apache.dubbo</groupId>
        <artifactId>dubbo-bom</artifactId>
        <version>3.3.3</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <properties>
    <java.version>1.8</java.version>
    <lombok.version>1.18.30</lombok.version>
  </properties>
</project>
```

---

## 🧭 10. Development Roadmap (Learning-Oriented)

| Week        | Task                                                  | Goal                             |
| ----------- | ----------------------------------------------------- | -------------------------------- |
| **Week 1**  | Setup parent POM, Nacos, Dubbo basic config           | Get services registered          |
| **Week 2**  | Implement `User-Service` (login/register)             | Understand REST + DB + Redis     |
| **Week 3**  | Implement `Product-Service`                           | Practice Dubbo provider          |
| **Week 4**  | Implement `Order-Service` + Dubbo calls               | Learn RPC & transaction handling |
| **Week 5**  | Add Kafka + cache optimization                        | Learn async messaging            |
| **Week 6**  | Add management endpoints, logging, exception handling | Production readiness             |
| **Week 7+** | Add Gateway + PF4J plugin module                      | Learn modular extensibility      |

---

## 🧰 11. Tools & Utilities

| Tool                     | Purpose                                      |
| ------------------------ | -------------------------------------------- |
| **Postman / curl**       | Test REST APIs                               |
| **Dubbo Admin Console**  | Monitor RPC services                         |
| **Nacos Console**        | Manage configs and service registry          |
| **Docker Compose**       | Spin up MySQL, Redis, Nacos, Kafka easily |
| **JVisualVM / JConsole** | Debug performance                            |

---

## 🧰 12. Flow Example: Order Creation via Dubbo

```declarative

[ HTTP POST /api/orders ]

{
"userId": 1,
"productId": 1001,
"quantity": 2
}


│
▼
┌────────────────────────────────────────────┐
│ OrderController (order-service)            │
│ - Handles endpoint: /api/orders            │
│ - Maps request body to CreateOrderRequest  │
│ - Calls OrderService.createOrder()         │
└────────────────────────────────────────────┘
│ calls
▼
┌────────────────────────────────────────────────┐
│ OrderService (order-service)                   │
│ - Contains business logic for orders           │
│ - Validates request & checks rules             │
│ - Calls ProductFacade.decreaseStock() via Dubbo RPC │
│ - Persists Order entity via OrderRepository    │
│ - Returns OrderDTO                             │   
└────────────────────────────────────────────────┘
│ uses
▼
┌────────────────────────────────────────────┐
│ @DubboReference ProductFacade              │
│ - Defined in common-dubbo-api module       │
│ - Interface shared across services         │
│ - Nacos handles provider discovery         │
└────────────────────────────────────────────┘
│
▼
( RPC over Dubbo + Nacos registry )
│
▼
┌────────────────────────────────────────────┐
│ @DubboService ProductFacadeImpl            │
│ (product-service)                          │
│ - Implements ProductFacade interface       │
│ - Decreases stock in DB for productId      │
│ - Exposed to other microservices via Dubbo │
└────────────────────────────────────────────┘
│
▼
┌────────────────────────────────────────────┐
│ ProductService + ProductRepository         │
│ (product-service)                          │
│ - Core product business logic              │
│ - Updates stock in MySQL via JPA           │
└────────────────────────────────────────────┘
│
▼
[ return void / success flag ]
│
▼
┌────────────────────────────────────────────────┐
│ OrderService (order-service)                   │
│ - Receives response from ProductFacade         │
│ - Completes Order creation & wraps as OrderDTO │
└────────────────────────────────────────────────┘
│
▼
┌────────────────────────────────────────────┐
│ OrderController (order-service)            │
│ - Returns OrderDTO as JSON to REST client  │
└────────────────────────────────────────────┘

```
