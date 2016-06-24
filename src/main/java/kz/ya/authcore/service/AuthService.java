/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.service;

import java.util.Calendar;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;
import kz.ya.authcore.entity.ApiToken;
import kz.ya.authcore.entity.User;
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
    public ApiToken login(String username, String password) throws LoginException {
        if (username == null || password == null) {
            throw new LoginException("Not null values are expected!");
        }
        User user = userFacade.find(username);
        if (user != null && user.getPassword().equals(password)) {
            /**
             * Once all params are matched, the authToken will be generated and
             * will be stored in the DB. The authToken will be needed for every
             * remote request and is only valid within the login session
             */
            String authToken = UUID.randomUUID().toString();
            if (user.getToken() == null) {
                user.setToken(new ApiToken(user));
            }
            user.getToken().setValue(authToken);

            // set issue date to current time
            Calendar calendar = Calendar.getInstance();
            user.getToken().setDateIssue(calendar.getTime());

            // set expiration date as after 14 days from now
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 14);
            user.getToken().setDateExpire(calendar.getTime());

            return user.getToken();
        }
        throw new LoginException("Don't Come Here Again!");
    }
}
