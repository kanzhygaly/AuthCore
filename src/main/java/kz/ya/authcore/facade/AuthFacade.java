/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.facade;

import java.util.Calendar;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;
import kz.ya.authcore.dao.HistoryDaoLocal;
import kz.ya.authcore.entity.ApiToken;
import kz.ya.authcore.entity.User;
import kz.ya.authcore.dao.UserDaoLocal;
import kz.ya.authcore.entity.History;

/**
 *
 * @author YERLAN
 */
@Stateless
public class AuthFacade implements AuthFacadeLocal {

    @EJB
    private UserDaoLocal userDao;
    @EJB
    private HistoryDaoLocal historyDao;
//    @EJB
//    private ApiTokenDaoLocal apiTokenDao;

    @Override
    public ApiToken login(String username, String password) throws LoginException {
        if (username == null || password == null) {
            throw new LoginException("Not null values are expected!");
        }
        User user = userDao.find(username);
        if (user != null && user.getPassword().equals(password)) {
            /**
             * Once all params are matched, the authToken will be generated and
             * will be stored in the DB. The authToken will be needed for every
             * remote request and is only valid within the login session
             */
            if (user.getToken() != null) {
                // save current token to history
                History history = new History(user, user.getToken().getValue(), 
                        user.getToken().getDateIssue(), user.getToken().getDateExpire());
                historyDao.save(history);
                
                // remove current token
                user.removeToken();
//                apiTokenDao.delete(user.getToken());
            }
            
            // create new token
            ApiToken token = new ApiToken(user);
            
            String authToken = UUID.randomUUID().toString();
            token.setValue(authToken);

            // set issue date to current time
            Calendar calendar = Calendar.getInstance();
            token.setDateIssue(calendar.getTime());

            // set expiration date as after 14 days from now
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 14);
            token.setDateExpire(calendar.getTime());
            
            // add token to user
            user.addToken(token);
            userDao.update(user);

            return user.getToken();
        }
        throw new LoginException("Don't Come Here Again!");
    }
}
