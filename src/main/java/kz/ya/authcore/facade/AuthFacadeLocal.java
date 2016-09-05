/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.facade;

import javax.ejb.Local;
import javax.security.auth.login.LoginException;
import kz.ya.authcore.entity.ApiToken;

/**
 *
 * @author YERLAN
 */
@Local
public interface AuthFacadeLocal {
    
    ApiToken login(String username, String password) throws LoginException;
}
