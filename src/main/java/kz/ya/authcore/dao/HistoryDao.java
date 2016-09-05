/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.ya.authcore.entity.History;

/**
 *
 * @author YERLAN
 */
@Stateless
public class HistoryDao extends AbstractDao<Long, History> implements HistoryDaoLocal {

    @Override
    public void update(History entity) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            History fromDB = (History) entityManager.find(History.class, entity.getId());
            fromDB.setValue(entity.getValue());
            fromDB.setDateIssue(entity.getDateIssue());
            fromDB.setDateExpire(entity.getDateExpire());

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
    }
}
