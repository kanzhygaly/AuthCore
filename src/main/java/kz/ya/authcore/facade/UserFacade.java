/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import static kz.ya.authcore.facade.AbstractFacade.getEntityManager;
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

    @Override
    public User find(String email) {
        User user = null;
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            Query query = entityManager.createNamedQuery("User.findById");
            query.setParameter("id", email);
            List<User> users = query.getResultList();
            if (!users.isEmpty()) {
                user = users.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
        return user;
    }
}
