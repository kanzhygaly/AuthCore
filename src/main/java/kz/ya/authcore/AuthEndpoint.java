package kz.ya.authcore;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.security.auth.login.LoginException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import kz.ya.authcore.entity.ApiToken;
import kz.ya.authcore.service.AuthServiceLocal;
import org.apache.log4j.Logger;

@ServerEndpoint("/wsauth")
public class AuthEndpoint {

    @Inject
    private AuthServiceLocal authService;
    private final Logger logger = Logger.getLogger(getClass());
    // this object will hold all WebSocket sessions connected to this WebSocket server endpoint (per JVM)
    private static final Set<Session> CLIENTS = Collections.synchronizedSet(new HashSet<Session>()); 

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        synchronized (CLIENTS) {
            String sequenceId = null, email = null, password = null;

            JsonObject jsonRequest;
            try (JsonReader reader = Json.createReader(new StringReader(message))) {
                jsonRequest = reader.readObject();
            }
            if (jsonRequest.getString("type").equals("LOGIN_CUSTOMER")) {
                sequenceId = jsonRequest.getString("sequence_id");
                System.out.println(sequenceId);

                JsonObject data = jsonRequest.getJsonObject("data");
                email = data.getString("email");
                System.out.println(email);
                password = data.getString("password");
                System.out.println(password);
            }

            // Iterate over the connected sessions and broadcast the received message
            for (Session client : CLIENTS) {
                if (client.equals(session)) {
                    String response;
                    try {
                        ApiToken token = authService.login(email, password);

                        JsonObject jsonResponse = Json.createObjectBuilder()
                                .add("type", "CUSTOMER_API_TOKEN")
                                .add("sequence_id", sequenceId)
                                .add("data", Json.createObjectBuilder()
                                        .add("api_token", token.getValue())
                                        .add("api_token_expiration_date", token.getDateExpire().toString())
                                        .build())
                                .build();

                        StringWriter stringWriter = new StringWriter();
                        try (JsonWriter writer = Json.createWriter(stringWriter)) {
                            writer.writeObject(jsonResponse);
                        }
                        response = stringWriter.getBuffer().toString();
                        
                    } catch (LoginException ex) {
                        logger.error(ex.getMessage());
                        
                        JsonObject jsonResponse = Json.createObjectBuilder()
                                .add("type", "CUSTOMER_ERROR")
                                .add("sequence_id", sequenceId)
                                .add("data", Json.createObjectBuilder()
                                        .add("error_description", "Customer not found")
                                        .add("error_code", "customer.notFound")
                                        .build())
                                .build();
                        
                        StringWriter stringWriter = new StringWriter();
                        try (JsonWriter writer = Json.createWriter(stringWriter)) {
                            writer.writeObject(jsonResponse);
                        }
                        response = stringWriter.getBuffer().toString();
                    }
                    
                    client.getBasicRemote().sendText(response);
                    break;
                }
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        // Add session to the connected sessions set
        CLIENTS.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        // Remove session from the connected sessions set
        CLIENTS.remove(session);
    }
}
