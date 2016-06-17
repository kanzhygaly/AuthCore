package kz.ya.authcore;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.security.auth.login.LoginException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketTest {

    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        synchronized (clients) {
            JsonReader reader = Json.createReader(new StringReader(message));
            JsonObject jsonMsg = reader.readObject();
            if (jsonMsg.getString("type").equals("LOGIN_CUSTOMER")) {
                System.out.println(jsonMsg.getString("sequence_id"));
                JsonObject data = jsonMsg.getJsonObject("data");
                System.out.println(data.getString("email"));
                System.out.println(data.getString("password"));
            }

            // Iterate over the connected sessions and broadcast the received message
            for (Session client : clients) {
//                if (!client.equals(session)) {
                client.getBasicRemote().sendText(message);
//                }
            }
        }

    }

    @OnOpen
    public void onOpen(Session session) {
        // Add session to the connected sessions set
        clients.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        // Remove session from the connected sessions set
        clients.remove(session);
    }

    // A user storage which stores <username, password>
    private final Map<String, String> usersStorage = new HashMap();
    // An authentication token storage which stores <service_key, auth_token>.
    private final Map<String, String> authorizationTokensStorage = new HashMap();

    public String login(String username, String password) throws LoginException {
        if (usersStorage.containsKey(username)) {
            String passwordMatch = usersStorage.get(username);

            if (passwordMatch.equals(password)) {

                /**
                 * Once all params are matched, the authToken will be generated
                 * and will be stored in the authorizationTokensStorage. The
                 * authToken will be needed for every REST API invocation and is
                 * only valid within the login session
                 */
                String authToken = UUID.randomUUID().toString();
                authorizationTokensStorage.put(authToken, username);

                return authToken;
            }
        }

        throw new LoginException("Don't Come Here Again!");
    }

    /**
     * The method that pre-validates if the client which invokes the REST API is
     * from a authorized and authenticated source.
     *
     * @param authToken The authorization token generated after login
     * @return TRUE for acceptance and FALSE for denied.
     */
    public boolean isAuthTokenValid(String authToken) {
        if (authorizationTokensStorage.containsKey(authToken)) {
            String usernameMatch2 = authorizationTokensStorage.get(authToken);

            if (usernameMatch2 != null) {
                return true;
            }
        }

        return false;
    }
}
