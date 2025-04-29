package unlp.info.bd2.repositories;

import java.util.Date;
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

    @Override
    @Transactional
    public void save(Object o) {
        sessionFactory.getCurrentSession().persist(o);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> Optional<T> findById(long id, Class<T> resultClass) {
        Session session = sessionFactory.getCurrentSession();
        Optional<T> object = session.createQuery(
                String.format("from %s where id = %d", resultClass.getName(), id), resultClass)
                .uniqueResultOptional();
        return object;
    }

    @Override
    @Transactional
    public void update(Object o) {
        sessionFactory.getCurrentSession().merge(o);
    }

    @Override
    @Transactional
    public void delete(Object o) {
        sessionFactory.getCurrentSession().remove(o);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Optional<User> user = session
                .createQuery(String.format("select distinct u from User u left join u.purchases p " +
                        "where u.username = '%s' and (u.active = true or p is not null)", username), User.class)
                .uniqueResultOptional();
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findManyByAtribute(Class<T> resultClass, String atributeName, String atributeValue){
        Session session = sessionFactory.getCurrentSession();
        List<T> result = session.createQuery(
                String.format("FROM %s WHERE %s LIKE :value", resultClass.getSimpleName(), atributeName), resultClass)
                .setParameter("value", atributeValue + "%")
                .getResultList();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public <T> Optional<T> findOneByAtribute(Class<T> resultClass, String atributeName, String atributeValue){
        Session session = sessionFactory.getCurrentSession();
        Optional<T> result = session.createQuery(
                        String.format("FROM %s WHERE %s LIKE :value", resultClass.getSimpleName(), atributeName), resultClass)
                .setParameter("value", atributeValue + "%")
                .uniqueResultOptional();
        return result;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price){
        Session session = sessionFactory.getCurrentSession();
        List<Route> result = session.createQuery(
                        String.format("FROM %s WHERE price < %f", Route.class.getSimpleName(), price), Route.class)
                .getResultList();
        return result;
    }

    @Override
    @Transactional
    public void addServiceToSupplier(Service s, Supplier supplier){
        Session session = sessionFactory.getCurrentSession();
        session.persist(s);
        supplier.getServices().add(s);
        update(s);
    }

    @Override
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

    @Override
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

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){
        Session session = sessionFactory.getCurrentSession();
        List<Purchase> result = session.createQuery(
                        "SELECT DISTINCT p FROM Purchase p JOIN p.itemServiceList is ORDER BY p.totalPrice DESC", Purchase.class)
                .setMaxResults(10)
                .getResultList();
        return result;
    }
//
    @Override
    @Transactional
    public void addItemToPurchase(ItemService item, float totalPrice, Purchase p){
        //revisar, si funciona ya lo del service borrar este
        Session session = sessionFactory.getCurrentSession();
        session.persist(item);
        p.getItemServiceList().add(item);
        //actualiza el precio del purchase con el item nuevo
        p.setTotalPrice(totalPrice);
        update(p);
        Service s = item.getService();
        s.addItem(item);
        update(s);
    }  

    @Override
    @Transactional
    public void createPurchase(Purchase p)throws ToursException{//REVISAR
        //ver cupos y ver except
        //purchases relacionadas a esa si 
        Session session = sessionFactory.getCurrentSession();
        String hql= "FROM Purchase p WHERE p.route = :ruta";
        List<Purchase> result = session.createQuery(hql, Purchase.class)
                .setParameter("ruta", p.getRoute())
                .getResultList();
        if (result.size() >= p.getRoute().getMaxNumberUsers()) {
            throw new ToursException("No hay mas cupos para la ruta con ID "+ p.getRoute().getId());
        }
        Long id= p.getUser().getId();
        User u= session.get(User.class, id); 
        save(p);
        u.addPurchase(p);
        update(u);

    }
    
    @Override
    @Transactional
    public  void addReviewToPurchase(Review review){ //Revisar
         //ver si solo add al purchase o si hacer todo junto aca, porque hace save en service de review, uno u otro
        Session session = sessionFactory.getCurrentSession();
        session.persist(review);
        Purchase p =review.getPurchase();
        p.setReview(review);
        update(p);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes(){
        Session session = sessionFactory.getCurrentSession();
        Long result = session.createQuery(
                        "SELECT COUNT(s) FROM Route r JOIN r.stops s GROUP BY r.id ORDER BY COUNT(s) DESC", Long.class)

                    .setMaxResults(1)
                    .getSingleResult();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell(){
        Session session = sessionFactory.getCurrentSession();
        List<Route> result = session.createQuery(
                        "SELECT r FROM Route r LEFT JOIN Purchase p ON p.route = r WHERE p.id IS NULL", Route.class)
                .getResultList();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop){
        Session session = sessionFactory.getCurrentSession();
        List<Route> result = session.createQuery(
                        "SELECT r FROM Route r JOIN r.stops s WHERE s.id = :stopId", Route.class)
                .setParameter("stopId", stop.getId())
                .getResultList();
        return result;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    @Transactional
    public void setDriverToRoute(DriverUser d, Route r){
        update(r);
        update(d);
    }

    @Override
    @Transactional
    public void setTourGuideToRoute(TourGuideUser t, Route r){
        update(t);
        update(r);
    }
    
    @Override
    @Transactional
    public void createUser(User u) throws ToursException{
        Session session = sessionFactory.getCurrentSession();
        Optional<User> us = findUserByUsername(u.getUsername());
        if (us.isPresent()){
            throw new ToursException("Ya existe un usuario con ese username");
        }
        else{
            save(u);
        }
    }

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
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
