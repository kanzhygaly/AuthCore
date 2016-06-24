/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.service;

import javax.ejb.Local;
import javax.security.auth.login.LoginException;
import kz.ya.authcore.entity.ApiToken;

/**
 *
 * @author YERLAN
 */
@Local
public interface AuthServiceLocal {
    
    ApiToken login(String username, String password) throws LoginException;
    
    /**
     * The method that pre-validates if the client which invokes the websocket is
     * from an authorized and authenticated source.
     *
     * @param authToken The authorization token generated after login
     * @return TRUE for acceptance and FALSE for denied.
     */
    boolean isAuthTokenValid(String authToken);
}
