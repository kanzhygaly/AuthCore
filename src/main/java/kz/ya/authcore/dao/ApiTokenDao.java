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
import kz.ya.authcore.entity.ApiToken;
import kz.ya.authcore.entity.User;

/**
 *
 * @author YERLAN
 */
@Stateless
public class ApiTokenDao extends AbstractDao<User, ApiToken> implements ApiTokenDaoLocal {

    @PersistenceContext(unitName = "auth_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void update(ApiToken entity) {
        ApiToken fromDB = findForUpdate(entity.getUser());
        fromDB.setValue(entity.getValue());
        fromDB.setDateIssue(entity.getDateIssue());
        fromDB.setDateExpire(entity.getDateExpire());
    }

    @Override
    public ApiToken find(String value) {
        ApiToken token = null;
        Query query = getEntityManager().createNamedQuery("ApiToken.findByValue");
        query.setParameter("value", value);
        List<ApiToken> tokens = query.getResultList();
        if (!tokens.isEmpty()) {
            token = tokens.get(0);
        }
        return token;
    }
}
