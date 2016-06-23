/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.service;

import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;
import kz.ya.authcore.facade.UserFacadeLocal;

/**
 *
 * @author YERLAN
 */
@Stateless
public class AuthService implements AuthServiceLocal {
    
    @EJB
    private UserFacadeLocal userFacade;

    @Override
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
}
