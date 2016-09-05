/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import static kz.ya.authcore.dao.AbstractDao.getEntityManager;
import kz.ya.authcore.entity.User;

/**
 *
 * @author YERLAN
 */
@Stateless
public class UserDao extends AbstractDao<Long, User> implements UserDaoLocal {
    
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
        Query query = getEntityManager().createNamedQuery("User.findByEmail");
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        if (!users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }
}