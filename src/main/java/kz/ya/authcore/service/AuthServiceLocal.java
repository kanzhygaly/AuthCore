/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.service;

import javax.ejb.Local;
import javax.security.auth.login.LoginException;

/**
 *
 * @author YERLAN
 */
@Local
public interface AuthServiceLocal {
    
    String login(String username, String password) throws LoginException;
}
