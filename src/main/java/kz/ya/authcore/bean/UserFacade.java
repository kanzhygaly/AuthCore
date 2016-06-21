/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import static kz.ya.authcore.bean.AbstractFacade.getEntityManager;
import kz.ya.authcore.entity.User;

/**
 *
 * @author YERLAN
 */
@Stateless
public class UserFacade extends AbstractFacade<Long, User> implements UserFacadeLocal {

    @Override
    public void update(User entity) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            User fromDB = (User) entityManager.find(User.class, entity.getId());
            fromDB.setEmail(entity.getEmail());
            fromDB.setPassword(entity.getPassword());

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
    }
}
