/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.ya.authcore.facade;

import java.util.List;
import javax.ejb.Local;
import kz.ya.authcore.entity.History;

/**
 *
 * @author YERLAN
 */
@Local
public interface HistoryFacadeLocal {

    History save(History token);

    void update(History token);
    
    void delete(History token);

    History find(Long id);

    History findForUpdate(Long id);

    List<History> findAll();

    List<History> findRange(int[] range);

    int count();
}
