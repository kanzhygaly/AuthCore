package kz.ya.authcore.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author Yerlan
 * @param <PK>
 * @param <T>
 */
public abstract class AbstractDao<PK extends Serializable, T> {

    private final Class<T> entityClass;

    public AbstractDao() {
        this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected abstract EntityManager getEntityManager();

    public T save(T entity) {
        if (!constraintValidationsDetected(entity)) {
            entity = getEntityManager().merge(entity);
        }
        return entity;
    }

    public abstract void update(T entity);

    public void delete(T entity) {
        if (!constraintValidationsDetected(entity)) {
            getEntityManager().remove(entity);
        }
    }

    public T find(PK key) {
        return (T) getEntityManager().find(entityClass, key);
    }

    public T findForUpdate(PK key) {
        return (T) getEntityManager().find(entityClass, key, LockModeType.PESSIMISTIC_WRITE);
    }

    public List<T> findAll() {
        EntityManager entityManager = getEntityManager();
        javax.persistence.criteria.CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        EntityManager entityManager = getEntityManager();
        javax.persistence.criteria.CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = entityManager.createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        EntityManager entityManager = getEntityManager();
        javax.persistence.criteria.CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = entityManager.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * <b>Entity validation method</b>
     * Method alerts which property and why it fails the validation
     *
     * @param entity
     * @return
     */
    private boolean constraintValidationsDetected(T entity) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (constraintViolations.size() > 0) {
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
            }
            return true;
        } else {
            return false;
        }
    }
}
