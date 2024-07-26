package com.example.library.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("WebSocket session established: " + session.getId());
    }


    public void sendNotification(String message) {
        System.out.println("Sending notification: " + message);
        for (WebSocketSession session : sessions) {
            try {
                Thread.sleep(5000);
                session.sendMessage(new TextMessage(message));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
