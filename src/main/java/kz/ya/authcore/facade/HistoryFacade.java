/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.ya.authcore.entity.History;

/**
 *
 * @author YERLAN
 */
@Stateless
public class HistoryFacade extends AbstractFacade<Long, History> implements HistoryFacadeLocal {

    @Override
    public void update(History entity) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            History fromDB = (History) entityManager.find(History.class, entity.getId());
            fromDB.setValue(entity.getValue());
            fromDB.setDateStart(entity.getDateStart());
            fromDB.setDateEnd(entity.getDateEnd());

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
    }
}