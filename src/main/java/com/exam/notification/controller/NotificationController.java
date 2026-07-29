package com.exam.notification.controller;

import com.exam.notification.dto.NotificationLogResponse;
import com.exam.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "API para la consulta de auditoría e historial de notificaciones enviadas")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Consultar historial de notificaciones por ID de Pedido")
    public ResponseEntity<List<NotificationLogResponse>> getLogsByOrderId(@PathVariable String orderId) {
        List<NotificationLogResponse> logs = notificationService.getLogsByOrderId(orderId);
        return ResponseEntity.ok(logs);
    }
}