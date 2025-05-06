package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
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

    @Override
    public void save(Object o) throws ToursException {
        try {
            // TODO CONSULTAR: ¿Está bien hacer un flush?
            sessionFactory.getCurrentSession().persist(o);
            sessionFactory.getCurrentSession().flush();
        } catch (ConstraintViolationException e) {
            throw new ToursException("Constraint Violation");
        }
        catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    public <T> Optional<T> findById(long id, Class<T> resultClass) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().find(resultClass, id));
    }

    @Override
    public <T> T update(T o) throws ToursException {
        try{
            return sessionFactory.getCurrentSession().merge(o);
        }
        catch (ConstraintViolationException e){
            throw new ToursException("Constraint Violation");
        }
        catch (Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    public void delete(Object o) throws ToursException {
        try {
            sessionFactory.getCurrentSession().remove(o);
        } catch (ConstraintViolationException e) {
            throw new ToursException("Constraint Violation");
        }
        catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Optional<User> user = session
                .createQuery(String.format("select distinct u from User u left join u.purchases p " +
                        "where u.username = '%s'", username), User.class)
                .uniqueResultOptional();
        return user;
    }

    @Override
    public Optional<TourGuideUser> findTourGuideByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Optional<TourGuideUser> user = session
                .createQuery(String.format("select distinct u from User u left join u.purchases p " +
                        "where u.username = '%s'", username), TourGuideUser.class)
                .uniqueResultOptional();
        return user;
    }

    @Override
    public Optional<DriverUser> findDriverByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Optional<DriverUser> user = session
                .createQuery(String.format("select distinct u from User u left join u.purchases p " +
                        "where u.username = '%s'", username), DriverUser.class)
                .uniqueResultOptional();
        return user;
    }

    @Override
    public <T> List<T> findManyByAtribute(Class<T> resultClass, String atributeName, String atributeValue){
        Session session = sessionFactory.getCurrentSession();
        List<T> result = session.createQuery(
                String.format("FROM %s WHERE %s LIKE :value", resultClass.getSimpleName(), atributeName), resultClass)
                .setParameter("value", atributeValue + "%")
                .getResultList();
        return result;
    }

    @Override
    public <T> Optional<T> findOneByAtribute(Class<T> resultClass, String atributeName, String atributeValue){
        Session session = sessionFactory.getCurrentSession();
        Optional<T> result = session.createQuery(
                        String.format("FROM %s WHERE %s LIKE :value", resultClass.getSimpleName(), atributeName), resultClass)
                .setParameter("value", atributeValue + "%")
                .uniqueResultOptional();
        return result;
    }
    
    @Override
    public List<Route> getRoutesBelowPrice(float price){
        Session session = sessionFactory.getCurrentSession();
        List<Route> result = session.createQuery(
                        String.format("FROM %s WHERE price < %f", Route.class.getSimpleName(), price), Route.class)
                .getResultList();
        return result;
    }

    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) {
        Session session = sessionFactory.getCurrentSession();
        Optional<Service> result = session.createQuery(
                    "FROM Service WHERE name LIKE :name AND supplier.id = :supplierId", Service.class)
                .setParameter("name", name)
                .setParameter("supplierId", id)
                .uniqueResultOptional();
        return result;
    }

    @Override
    public boolean existeSupplier(String authNumber){
        Session session = sessionFactory.getCurrentSession();
        Optional<Supplier> result = session.createQuery(
                        "FROM Supplier WHERE authorizationNumber LIKE :authorizationNumber", Supplier.class)
                .setParameter("authorizationNumber", authNumber)
                .uniqueResultOptional();
        return result.isPresent();
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        Session session = sessionFactory.getCurrentSession();
        List<Supplier> result = session.createQuery(
                "SELECT s FROM Supplier s JOIN s.services ser JOIN ser.itemServices is JOIN is.purchase p " +
                        "GROUP BY s.id " +
                        "ORDER BY SUM(p.totalPrice) DESC", Supplier.class)
                .setMaxResults(n)
                .getResultList();
        return result;
    }

    @Override
    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){
        Session session = sessionFactory.getCurrentSession();
        List<Purchase> result = session.createQuery(
                        "SELECT DISTINCT p FROM Purchase p JOIN p.itemServiceList is ORDER BY p.totalPrice DESC", Purchase.class)
                .setMaxResults(10)
                .getResultList();
        return result;
    }

    @Override
    public boolean canCreatePurchase(Date date, Route route) {//REVISAR
        Session session = sessionFactory.getCurrentSession();
        String hql= "FROM Purchase p WHERE p.route = :ruta and p.date = :date";
        List<Purchase> result = session.createQuery(hql, Purchase.class)
                .setParameter("ruta", route)
                .setParameter("date", date)
                .getResultList();
        if (result.size() >= route.getMaxNumberUsers()) {
            return false;
        }
        else{
            return true;
        }
    }


    @Override
    public Long getMaxStopOfRoutes(){
        Session session = sessionFactory.getCurrentSession();
        Long result = session.createQuery(
                        "SELECT COUNT(s) FROM Route r JOIN r.stops s GROUP BY r.id ORDER BY COUNT(s) DESC", Long.class)
                    .setMaxResults(1)
                    .getSingleResult();
        return result;
    }

    @Override
    public List<Route> getRoutsNotSell(){
        Session session = sessionFactory.getCurrentSession();
        List<Route> result = session.createQuery(
                        "SELECT r FROM Route r LEFT JOIN Purchase p ON p.route = r WHERE p.id IS NULL", Route.class)
                .getResultList();
        return result;
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop){
        Session session = sessionFactory.getCurrentSession();
        List<Route> result = session.createQuery(
                        "SELECT r FROM Route r JOIN r.stops s WHERE s.id = :stopId", Route.class)
                .setParameter("stopId", stop.getId())
                .getResultList();
        return result;
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        Session session = sessionFactory.getCurrentSession();
        List<User> result = session.createQuery(
                "SELECT DISTINCT u "+
                "FROM User u JOIN u.purchases p " +
                "WHERE p.totalPrice >= :mount",
                User.class)
                .setParameter("mount", mount)
                .getResultList();
        return result;
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        Session session = sessionFactory.getCurrentSession();
        List<Route> result = session.createQuery(
                "SELECT rte FROM Purchase p JOIN p.route rte JOIN p.review rev " +
                        "GROUP BY rte.id " +
                        "ORDER BY AVG(rev.rating) DESC",
                Route.class)
                .setMaxResults(3)
                .getResultList();
        return result;
    }

    @Override
    public Service getMostDemandedService() {
        Session session = sessionFactory.getCurrentSession();
        Service result = session.createQuery(
                "FROM Service s JOIN s.itemServices item " +
                        "GROUP BY s.id " +
                        "ORDER BY SUM(item.quantity) DESC",
                Service.class)
                .setMaxResults(1)
                .uniqueResult();
        return result;
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        Session session = sessionFactory.getCurrentSession();
        List<TourGuideUser> result = session.createQuery(
            "SELECT distinct u FROM Review r JOIN r.purchase p JOIN p.route rou JOIN rou.tourGuides u " +
            "WHERE r.rating = 1",
            TourGuideUser.class)
                .getResultList();
        return result;
    }

    @Override
    public List<User> getTop5UsersMorePurchases(){
        Session session = sessionFactory.getCurrentSession();
        List<User> result = session.createQuery(
                "FROM User u JOIN u.purchases p " +
                        "GROUP BY u " +
                        "ORDER BY count(p) DESC ",
                User.class)
                .setMaxResults(5)
                .getResultList();
        return result;
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end){
        Session session = sessionFactory.getCurrentSession();
        Long result = session.createQuery(
                "SELECT COUNT(p)"+
                "FROM Purchase p "+
                "WHERE p.date BETWEEN :start AND :end", 
                Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();  
        return result;

    }

    @Override
    public List<Service> getServiceNoAddedToPurchases(){
        Session session = sessionFactory.getCurrentSession();
        List<Service> result = session.createQuery(
                "SELECT s "+
                "FROM Service s "+
                "LEFT JOIN ItemService i ON i.service = s " +
                "WHERE i.id IS NULL" ,
                Service.class)
                .getResultList();
        return result;
    }
}
