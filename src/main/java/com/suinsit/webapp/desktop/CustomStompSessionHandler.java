package com.suinsit.webapp.desktop;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomStompSessionHandler extends StompSessionHandlerAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomStompSessionHandler.class);
    private final AtomicInteger connectionAttempts = new AtomicInteger(0);
    private final ConcurrentHashMap<String, Listbox> messageListboxes = new ConcurrentHashMap<>();
    private final int MAX_MESSAGES = 100; // Límite de mensajes en la UI
    
    private Listbox messageListbox;
    private Desktop desktop;

    public CustomStompSessionHandler(Listbox messageListbox, Desktop desktop) {
        this.messageListbox = messageListbox;
        this.desktop = desktop;
        this.messageListboxes.put(desktop.getId(), messageListbox);
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("Nueva sesión STOMP establecida: " + session.getSessionId());
        connectionAttempts.set(0);

        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }
            
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message message = (Message) payload;
                try {
                    // Actualizar UI en el hilo de ZK
                    Executions.schedule(desktop, event -> {
                        updateUI(message);
                    }, new Event("updateUI"));
                } catch (Exception e) {
                    logger.error("Error al manejar mensaje WebSocket", e);
                }
            }
        });
    }

    @Override
    public void handleException(StompSession session, StompCommand command, 
                              StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Error en sesión STOMP", exception);
        handleReconnection(session);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        logger.error("Error de transporte STOMP", exception);
        handleReconnection(session);
    }

    private void handleReconnection(StompSession session) {
        int attempts = connectionAttempts.incrementAndGet();
        if (attempts <= 5) {
            long backoffTime = (long) Math.pow(2, attempts) * 1000;
            try {
                Thread.sleep(backoffTime);
                // Implementar lógica de reconexión
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    protected void updateUI(Message message) {
        try {
            if (!desktop.isServerPushEnabled()) {
                desktop.enableServerPush(true);
            }

            Listbox listbox = messageListboxes.get(desktop.getId());
            if (listbox != null) {
                // Crear nuevo item para el mensaje
                Listitem item = new Listitem();
                
                // Agregar células con la información del mensaje
                item.appendChild(new Listcell(message.getSender()));
                item.appendChild(new Listcell(message.getContent()));
                item.appendChild(new Listcell(formatTimestamp(message.getTimestamp())));
                
                // Aplicar estilos según el tipo de mensaje
                applyMessageStyles(item, message);
                
                // Agregar al inicio de la lista
                listbox.insertBefore(item, listbox.getFirstChild());
                
                // Mantener límite de mensajes
                while (listbox.getItemCount() > MAX_MESSAGES) {
                    listbox.removeItemAt(listbox.getItemCount() - 1);
                }
                
                // Scroll al último mensaje
                listbox.setSelectedItem(item);
            }
        } catch (Exception e) {
            logger.error("Error actualizando UI", e);
        }
    }

    private void applyMessageStyles(Listitem item, Message message) {
        // Ejemplo de estilos condicionales
        if (message.isError()) {
            item.setStyle("background-color: #ffebee;"); // Rojo claro para errores
        } else if (message.isSystem()) {
            item.setStyle("background-color: #e3f2fd;"); // Azul claro para mensajes del sistema
        }
        
        // Agregar clases CSS según el tipo de mensaje
        item.setSclass("message-item " + message.getType().name().toLowerCase());
    }

    private String formatTimestamp(String timestamp) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(timestamp);
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        } catch (Exception e) {
            return timestamp;
        }
    }

    // Método para limpiar recursos
    public void cleanup() {
        messageListboxes.remove(desktop.getId());
        if (desktop.isServerPushEnabled()) {
            desktop.enableServerPush(false);
        }
    }
}
