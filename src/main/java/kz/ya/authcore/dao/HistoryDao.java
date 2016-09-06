/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kz.ya.authcore.entity.History;

/**
 *
 * @author YERLAN
 */
@Stateless
public class HistoryDao extends AbstractDao<Long, History> implements HistoryDaoLocal {

    @PersistenceContext(unitName = "auth_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void update(History entity) {
        History fromDB = findForUpdate(entity.getId());
        fromDB.setValue(entity.getValue());
        fromDB.setDateIssue(entity.getDateIssue());
        fromDB.setDateExpire(entity.getDateExpire());
    }
}
