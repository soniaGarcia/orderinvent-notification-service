package com.exam.notification.listener;

import com.exam.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consumeOrderEvent(String rawPayload) {
        log.info("Mensaje recibido desde Kafka Topic [order-events]: {}", rawPayload);
        notificationService.processOrderEvent(rawPayload);
    }
}