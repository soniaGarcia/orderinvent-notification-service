# Notification Service

Microservicio desacoplado de auditoría y notificaciones. Consume de forma asíncrona todos los eventos del dominio emitidos en el sistema para mantener la trazabilidad histórica del pedido y despachar alertas a clientes.

## 🚀 Tecnologías
* **Runtime:** Java 21 / Spring Boot 3.x
* **Base de Datos:** PostgreSQL (Aurora Serverless v2)
* **Mensajería:** Apache Kafka (Consumer Group dedicado)

## ⚙️ Puerto y Endpoints
* **Puerto Local:** `8082`
* **Consola H2 (Dev):** `http://localhost:8082/h2-console` (JDBC URL: `jdbc:h2:mem:notification_db`)
* **Endpoints HTTP:**
  * `GET /api/v1/notifications/order/{orderId}` - Auditoría de orden.
  * `GET /actuator/health/readiness` - Health Check.

## 🛠️ Variables de Entorno Clave (Perfil `local`)
```env
SERVER_PORT=8082
SPRING_PROFILES_ACTIVE=local
SPRING_DATASOURCE_URL=jdbc:h2:mem:notification_db
SPRING_H2_CONSOLE_ENABLED=true
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092