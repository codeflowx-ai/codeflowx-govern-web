package com.suinsit.webapp.desktop;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String content;
    private String timestamp;
    private String sender;
    private MessageType type;
    private boolean error;
    private boolean system;
    
    public enum MessageType {
        NORMAL,
        ERROR,
        SYSTEM,
        WARNING,
        INFO
    }
    
    // Constructor conveniente para mensajes simples
    public Message(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now().toString();
        this.type = MessageType.NORMAL;
        this.error = false;
        this.system = false;
    }
    
    // Constructor para mensajes del sistema
    public static Message systemMessage(String content) {
        Message message = new Message(content);
        message.setSystem(true);
        message.setType(MessageType.SYSTEM);
        message.setSender("System");
        return message;
    }
    
    // Constructor para mensajes de error
    public static Message errorMessage(String content) {
        Message message = new Message(content);
        message.setError(true);
        message.setType(MessageType.ERROR);
        message.setSender("Error");
        return message;
    }
}