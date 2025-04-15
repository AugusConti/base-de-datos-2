package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;

public class ToursRepositoryImpl implements ToursRepository {

    @Autowired
    LocalSessionFactoryBean sessionFactoryBean;

    public void save(Object o) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        session.persist(o);
        session.getTransaction().commit();
        session.close();
    }

    public <T> Optional<T> findById(long id, Class<T> resultClass) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        Optional<T> object = session.createQuery(
                String.format("from %s where id = %d", resultClass.getName(), id), resultClass).uniqueResultOptional();
        session.getTransaction().commit();
        return object;
    }

    public void update(Object o) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        session.merge(o);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Object o) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        session.remove(o);
        session.getTransaction().commit();
        session.close();
    }

    public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        Optional<User> user = session
                .createQuery(String.format("from User where username = '%s'", username), User.class)
                .uniqueResultOptional();
        session.getTransaction().commit();
        return user;
    }
    public <T> List<T> findManyByAtribute(Class<T> resultClass, String atributeName, String atributeValue){
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        List<T> result = session.createQuery(
                String.format("FROM %s WHERE %s LIKE :value", resultClass.getSimpleName(), atributeName), resultClass)
                .setParameter("value", atributeValue + "%")
                .getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }
    public <T> Optional<T> findOneByAtribute(Class<T> resultClass, String atributeName, String atributeValue){
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        Optional<T> result = session.createQuery(
                        String.format("FROM %s WHERE %s LIKE :value", resultClass.getSimpleName(), atributeName), resultClass)
                .setParameter("value", atributeValue + "%")
                .uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        return result;
    }
    public List<Route> getRoutesBelowPrice(float price){
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        List<Route> result = session.createQuery(
                        String.format("FROM %s WHERE price < %f", Route.class.getSimpleName(), price), Route.class)
                .getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }
    public void addServiceToSupplier(Service s, Supplier supplier){
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        session.persist(s);
        supplier.getServices().add(s);
        session.merge(supplier);
        session.getTransaction().commit();
        session.close();
    }
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException{
        //Optional<Supplier> s = findById(id, Supplier.class);
        //if(s == null){
        //    throw new ToursException("No existe un Supplier con id: "+id);
        //}
        Session session = sessionFactoryBean.getObject().openSession();
        try {
            session.getTransaction().begin();
            Optional<Service> result = session.createQuery(
                            "FROM Service WHERE name LIKE :name AND supplier.id = :supplierId", Service.class)
                    .setParameter("name", name)
                    .setParameter("supplierId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
            if //(result.isEmpty()) {
            (false){
                throw new ToursException("No existe un Supplier con id: " + id + " Y/o un Service con nombre: " + name);
            }
            return result;
        }
        finally {
            session.close();
        }
    }
    public void createSupplier(Supplier s) throws ToursException{
        Session session = sessionFactoryBean.getObject().openSession();
        try {
            session.getTransaction().begin();
            Optional<Supplier> result = session.createQuery(
                            "FROM Supplier WHERE authorizationNumber LIKE :authorizationNumber", Supplier.class)
                    .setParameter("authorizationNumber", s.getAuthorizationNumber())
                    .uniqueResultOptional();
            session.getTransaction().commit();
            if (result.isPresent()) {
                throw new ToursException("Ya existe un Supplier con authorizationNumber: " + s.getAuthorizationNumber());
            }
            session.getTransaction().begin();
            save(s);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        Session session = sessionFactoryBean.getObject().openSession();
        session.getTransaction().begin();
        List<Supplier> result = session.createQuery(
                "FROM Supplier s JOIN s.services ser JOIN ser.itemServices is JOIN is.purchase p" +
                        "GROUP BY s.id" +
                        "ORDER BY SUM(p.totalPrice) DESC", Supplier.class)
                .getResultList();
        session.getTransaction().commit();
        session.close();
        return result;
    }
}
