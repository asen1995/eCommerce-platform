package com.ecommerence.platform.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class BackOfficeWebSocketClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${back.office.service.name}")
    private String backOfficeServiceName;

    private StompSession session;

    @PostConstruct
    public void init() throws InterruptedException, ExecutionException {

        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StringMessageSessionHandler sessionHandler = new StringMessageSessionHandler();

        session = stompClient.connect(getWebSocketUrl(backOfficeServiceName), sessionHandler).get();
    }

    @PreDestroy
    public void close() {
        if (session != null) {
            session.disconnect();
        }
    }

    public void sendMessage(String destination, String message) {
        session.send(destination, message);
    }


    private String getWebSocketUrl(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances.isEmpty()) {

            throw new IllegalStateException("No instances available for service: " + serviceName);
        }

        ServiceInstance instance = instances.get(0);
        String hostname = instance.getHost();
        int port = instance.getPort();

        return String.format("ws://%s:%d/websocket-server", hostname, port);
    }
}

class StringMessageSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("Received : " + payload);
    }
}
