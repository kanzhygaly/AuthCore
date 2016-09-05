package kz.ya.authcore;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.websocket.EncodeException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import kz.ya.authcore.dto.ErrorData;
import kz.ya.authcore.dto.InData;
import kz.ya.authcore.dto.Message;
import kz.ya.authcore.dto.SuccessData;
import kz.ya.authcore.entity.ApiToken;
import kz.ya.authcore.util.MessageDecoder;
import kz.ya.authcore.util.MessageEncoder;
import org.apache.log4j.Logger;
import kz.ya.authcore.facade.AuthFacadeLocal;

@ServerEndpoint(value = "/wsauth", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class AuthEndpoint {

    @Inject
    private AuthFacadeLocal authFacade;
    private final Logger logger = Logger.getLogger(getClass());
    // this object will hold all WebSocket sessions connected to this WebSocket server endpoint (per JVM)
    private static final Set<Session> CLIENTS = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException {

        synchronized (CLIENTS) {
            // Iterate over the connected sessions and broadcast the received message
            for (Session client : CLIENTS) {
                if (client.equals(session)) {
                    if (message == null || !message.getType().equals("LOGIN_CUSTOMER")) {
                        try {
                            client.getBasicRemote().sendObject(message);
                        } catch (EncodeException ex) {
                            logger.error(ex.getMessage(), ex);
                        }
                        return;
                    }
                    InData inData = (InData) message.getData();

                    Message response = new Message(message.getSequenceId());
                    try {
                        ApiToken token = authFacade.login(inData.getEmail(), inData.getPassword());

                        response.setType("CUSTOMER_API_TOKEN");

                        SuccessData data = new SuccessData(token.getValue(), token.getDateExpire().toString());
                        response.setData(data);

                    } catch (LoginException ex) {
                        response.setType("CUSTOMER_ERROR");

                        ErrorData data = new ErrorData("Customer not found", "customer.notFound");
                        response.setData(data);
                    }

                    try {
                        client.getBasicRemote().sendObject(response);
                    } catch (EncodeException ex) {
                        logger.error(ex.getMessage(), ex);
                    }
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
