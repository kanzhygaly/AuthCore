/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.facade;

import java.util.List;
import javax.ejb.Local;
import kz.ya.authcore.entity.ApiToken;
import kz.ya.authcore.entity.User;

/**
 *
 * @author YERLAN
 */
@Local
public interface ApiTokenFacadeLocal {

    ApiToken save(ApiToken token);

    void update(ApiToken token);
    
    void delete(ApiToken token);

    ApiToken find(User user);

    ApiToken findForUpdate(User user);

    List<ApiToken> findAll();

    List<ApiToken> findRange(int[] range);

    int count();
    
    ApiToken find(String value);
}
