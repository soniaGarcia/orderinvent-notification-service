package com.exam.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLogResponse {
    private Long id;
    private String orderId;
    private String orderStatus;
    private String channel;
    private String status;
    private String messageContent;
    private LocalDateTime createdAt;
}