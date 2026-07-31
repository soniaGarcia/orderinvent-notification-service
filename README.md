# Notification Service

Microservicio desacoplado de auditoría y notificaciones. Consume de forma asíncrona todos los eventos del dominio emitidos en el sistema para mantener la trazabilidad histórica del pedido y despachar alertas a clientes.

## 🚀 Tecnologías
* **Runtime:** Java 21 / Spring Boot 3.x
* **Base de Datos:** PostgreSQL (Aurora Serverless v2)
* **Mensajería:** Apache Kafka (Consumer Group dedicado)

## ⚙️ Puerto y Endpoints
* **Puerto Local:** `8082`
* **Endpoints HTTP:**
  * `GET /api/v1/notifications/order/{orderId}` - Consulta de historial de auditoría por orden.
  * `GET /actuator/health/readiness` - Health Check ALB.

## 🔄 Integración de Eventos (Kafka)
* **Consumidor:** `order-events` e `inventory-events` (Registra todos los cambios de estado sin bloquear el flujo principal).

## 🛠️ Variables de Entorno Clave
```env
SERVER_PORT=8082
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/notification_db
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092