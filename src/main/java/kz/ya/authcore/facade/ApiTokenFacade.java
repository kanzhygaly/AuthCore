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
import kz.ya.authcore.entity.ApiToken;
import kz.ya.authcore.entity.User;
import static kz.ya.authcore.facade.AbstractFacade.getEntityManager;

/**
 *
 * @author YERLAN
 */
@Stateless
public class ApiTokenFacade extends AbstractFacade<User, ApiToken> implements ApiTokenFacadeLocal {

    @Override
    public void update(ApiToken entity) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            ApiToken fromDB = (ApiToken) entityManager.find(ApiToken.class, entity.getUser());
            fromDB.setValue(entity.getValue());
            fromDB.setDateIssue(entity.getDateIssue());
            fromDB.setDateExpire(entity.getDateExpire());

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public ApiToken find(String value) {
        ApiToken token = null;
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            Query query = entityManager.createNamedQuery("ApiToken.findByValue");
            query.setParameter("value", value);
            List<ApiToken> tokens = query.getResultList();
            if (!tokens.isEmpty()) {
                token = tokens.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
        return token;
    }
}
