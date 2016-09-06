/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import kz.ya.authcore.entity.User;

/**
 *
 * @author YERLAN
 */
@Stateless
public class UserDao extends AbstractDao<Long, User> implements UserDaoLocal {

    @PersistenceContext(unitName = "auth_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void update(User entity) {
        User fromDB = findForUpdate(entity.getId());
        fromDB.setEmail(entity.getEmail());
        fromDB.setPassword(entity.getPassword());
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
