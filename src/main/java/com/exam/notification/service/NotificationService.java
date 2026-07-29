package com.exam.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.exam.notification.dto.NotificationLogResponse;
import com.exam.notification.dto.OrderEventPayload;
import com.exam.notification.model.NotificationChannel;
import com.exam.notification.model.NotificationLog;
import com.exam.notification.model.NotificationStatus;
import com.exam.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationLogRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void processOrderEvent(String rawJsonPayload) {
        try {
            OrderEventPayload payload = objectMapper.readValue(rawJsonPayload, OrderEventPayload.class);
            log.info("Procesando evento de notificación para el Pedido ID: {} con Estado: {}", 
                    payload.getOrderId(), payload.getStatus());

            // Patron Idempotencia: Verificar si este evento ya fue notificado con éxito
            boolean alreadyProcessed = repository.existsByOrderIdAndOrderStatusAndStatus(
                    payload.getOrderId(), payload.getStatus(), NotificationStatus.SENT);

            if (alreadyProcessed) {
                log.warn("Evento duplicado detectado para Pedido ID: {}. Se omite el envío repetido.", payload.getOrderId());
                repository.save(NotificationLog.builder()
                        .orderId(payload.getOrderId())
                        .orderStatus(payload.getStatus())
                        .channel(NotificationChannel.EMAIL)
                        .status(NotificationStatus.SKIPPED_DUPLICATE)
                        .messageContent("Evento duplicado ignorado.")
                        .build());
                return;
            }

            // Simulación de envío de correo/SMS
            sendExternalNotification(payload);

            // Registro de Auditoría
            NotificationLog logEntry = NotificationLog.builder()
                    .orderId(payload.getOrderId())
                    .orderStatus(payload.getStatus())
                    .channel(NotificationChannel.EMAIL)
                    .status(NotificationStatus.SENT)
                    .messageContent(payload.getMessage())
                    .build();

            repository.save(logEntry);
            log.info("Notificación registrada exitosamente en la base de datos para el pedido {}", payload.getOrderId());

        } catch (JsonProcessingException e) {
            log.error("Error al deserializar el evento JSON recibido desde Kafka: {}", rawJsonPayload, e);
        } catch (Exception e) {
            log.error("Error al despachar la notificación", e);
        }
    }

    private void sendExternalNotification(OrderEventPayload payload) {
        log.info("----------------------------------------------------------------------");
        log.info(">>> SIMULANDO ENVÍO DE EMAIL / SMS A CLIENTE <<<");
        log.info("Mensaje: Pedido #{} ha cambiado a estado '{}'. Detalle: {}", 
                payload.getOrderId(), payload.getStatus(), payload.getMessage());
        log.info("----------------------------------------------------------------------");
    }

    @Transactional(readOnly = true)
    public List<NotificationLogResponse> getLogsByOrderId(String orderId) {
        return repository.findByOrderId(orderId).stream()
                .map(log -> NotificationLogResponse.builder()
                        .id(log.getId())
                        .orderId(log.getOrderId())
                        .orderStatus(log.getOrderStatus())
                        .channel(log.getChannel().name())
                        .status(log.getStatus().name())
                        .messageContent(log.getMessageContent())
                        .createdAt(log.getCreatedAt())
                        .build())
                .toList();
    }
}