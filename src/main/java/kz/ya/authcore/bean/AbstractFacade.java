package kz.ya.authcore.bean;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
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
public abstract class AbstractFacade<PK extends Serializable, T> {

    private final Class<T> entityClass;

    public AbstractFacade() {
        this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    private static final EntityManagerFactory EMFACTORY;

    static {
        try {
            EMFACTORY = Persistence.createEntityManagerFactory("auth_pu");

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return EMFACTORY.createEntityManager();
    }

    public T save(T entity) {
        if (!constraintValidationsDetected(entity)) {
            EntityManager entityManager = getEntityManager();
            try {
                entityManager.getTransaction().begin();

                entity = entityManager.merge(entity);

                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                entityManager.getTransaction().rollback();
            }
        }
        return entity;
    }
    
    public abstract void update(T entity);

    public void delete(T entity) {
        if (!constraintValidationsDetected(entity)) {
            EntityManager entityManager = getEntityManager();
            try {
                entityManager.getTransaction().begin();

                entityManager.remove(entity);

                entityManager.getTransaction().commit();
            } catch (Exception ex) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public T find(PK key) {
        T entity = null;
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            entity = (T) entityManager.find(entityClass, key);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
        return entity;
    }

    public T findForUpdate(PK key) {
        T entity = null;
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            entity = (T) entityManager.find(entityClass, key, LockModeType.PESSIMISTIC_WRITE);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
        return entity;
    }

    public List<T> findAll() {
        List<T> list = null;
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            javax.persistence.criteria.CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            list = entityManager.createQuery(cq).getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
        return list;
    }

    public List<T> findRange(int[] range) {
        List<T> list = null;
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            javax.persistence.criteria.CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            javax.persistence.Query q = entityManager.createQuery(cq);
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
            list = q.getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
        return list;
    }

    public int count() {
        int count = 0;
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            javax.persistence.criteria.CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
            javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
            cq.select(entityManager.getCriteriaBuilder().count(rt));
            javax.persistence.Query q = entityManager.createQuery(cq);
            count = ((Long) q.getSingleResult()).intValue();

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
        return count;
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
