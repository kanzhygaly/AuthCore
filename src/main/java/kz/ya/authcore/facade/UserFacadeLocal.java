/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.facade;

import java.util.List;
import javax.ejb.Local;
import kz.ya.authcore.entity.User;

/**
 *
 * @author YERLAN
 */
@Local
public interface UserFacadeLocal {

    User save(User user);

    void update(User user);
    
    void delete(User user);

    User find(Long id);

    User findForUpdate(Long id);

    List<User> findAll();

    List<User> findRange(int[] range);

    int count();
    
    User find(String email);
}
