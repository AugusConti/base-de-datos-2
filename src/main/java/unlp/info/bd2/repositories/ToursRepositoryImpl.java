package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import org.springframework.transaction.annotation.Transactional;
import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;
import org.springframework.stereotype.Repository;

@Repository
public class ToursRepositoryImpl implements ToursRepository {

    @Autowired
    private org.hibernate.SessionFactory sessionFactory;

    @Transactional
    public void save(Object o) {
        sessionFactory.getCurrentSession().persist(o);
    }
    @Transactional(readOnly = true)
    public <T> Optional<T> findById(long id, Class<T> resultClass) {
        Session session = sessionFactory.getCurrentSession();
        Optional<T> object = session.createQuery(
                String.format("from %s where id = %d", resultClass.getName(), id), resultClass)
                .uniqueResultOptional();
        return object;
    }

    @Transactional
    public void update(Object o) {
        sessionFactory.getCurrentSession().merge(o);
    }

    @Transactional
    public void delete(Object o) {
        sessionFactory.getCurrentSession().remove(o);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Optional<User> user = session
                .createQuery(String.format("from User where username = '%s'", username), User.class)
                .uniqueResultOptional();
        return user;
    }

    @Transactional(readOnly = true)
    public <T> List<T> findManyByAtribute(Class<T> resultClass, String atributeName, String atributeValue){
        Session session = sessionFactory.getCurrentSession();
        List<T> result = session.createQuery(
                String.format("FROM %s WHERE %s LIKE :value", resultClass.getSimpleName(), atributeName), resultClass)
                .setParameter("value", atributeValue + "%")
                .getResultList();
        return result;
    }

    @Transactional(readOnly = true)
    public <T> Optional<T> findOneByAtribute(Class<T> resultClass, String atributeName, String atributeValue){
        Session session = sessionFactory.getCurrentSession();
        Optional<T> result = session.createQuery(
                        String.format("FROM %s WHERE %s LIKE :value", resultClass.getSimpleName(), atributeName), resultClass)
                .setParameter("value", atributeValue + "%")
                .uniqueResultOptional();
        return result;
    }

    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price){
        Session session = sessionFactory.getCurrentSession();
        List<Route> result = session.createQuery(
                        String.format("FROM %s WHERE price < %f", Route.class.getSimpleName(), price), Route.class)
                .getResultList();
        return result;
    }

    @Transactional
    public void addServiceToSupplier(Service s, Supplier supplier){
        Session session = sessionFactory.getCurrentSession();
        session.persist(s);
        supplier.getServices().add(s);
        session.merge(supplier);
    }

    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException{
        Optional<Supplier> s = findById(id, Supplier.class);
        if(s.isEmpty()){
            throw new ToursException("No existe un Supplier con id: "+id);
        }
        Session session = sessionFactory.getCurrentSession();
        Optional<Service> result = session.createQuery(
                    "FROM Service WHERE name LIKE :name AND supplier.id = :supplierId", Service.class)
                .setParameter("name", name)
                .setParameter("supplierId", id)
                .uniqueResultOptional();
        return result;
    }

    @Transactional
    public void createSupplier(Supplier s) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
            Optional<Supplier> result = session.createQuery(
                            "FROM Supplier WHERE authorizationNumber LIKE :authorizationNumber", Supplier.class)
                    .setParameter("authorizationNumber", s.getAuthorizationNumber())
                    .uniqueResultOptional();
            if (result.isPresent()) {
                throw new ToursException("Ya existe un Supplier con authorizationNumber: " + s.getAuthorizationNumber());
            }
            save(s);
    }

    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        Session session = sessionFactory.getCurrentSession();
        List<Supplier> result = session.createQuery(
                "FROM Supplier s JOIN s.services ser JOIN ser.itemServices is JOIN is.purchase p" +
                        "GROUP BY s.id" +
                        "ORDER BY SUM(p.totalPrice) DESC", Supplier.class)
                .getResultList();
        return result;
    }

    @Transactional(readOnly = true)
    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){
        Session session = sessionFactory.getCurrentSession();
        List<Purchase> result = session.createQuery(
                        "FROM purchase p ORDER BY p.totalPrice DESC LIMIT 10", Purchase.class)
                .getResultList();
        return result;
    }
}
