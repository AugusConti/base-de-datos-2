package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;

public interface ToursRepository {
    void save(Object o);
    <T> Optional<T> findById(long id, Class<T> _class);
    void update(Object o);
    void delete(Object o);

    Optional<User> findUserByUsername(String username);
    <T> List<T> findManyByAtribute(Class<T> resultClass, String atributeName, String atributeValue);
    <T> Optional<T> findOneByAtribute(Class<T> resultClass, String atributeName, String atributeValue);
    List<Route> getRoutesBelowPrice(float price);
    void addServiceToSupplier(Service s, Supplier supplier);
    Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException;
    void createSupplier(Supplier s) throws ToursException;
    List<Supplier> getTopNSuppliersInPurchases(int n);
    List<User> getUserSpendingMoreThan(float mount);
    List<Route> getTop3RoutesWithMaxRating();
    Service getMostDemandedService();
    List<TourGuideUser> getTourGuidesWithRating1();
    List<Purchase> getTop10MoreExpensivePurchasesInServices();
    void addItemToPurchase(ItemService i,float totalPrice);
    void createPurchase(Purchase p)throws ToursException;
    void addReviewToPurchase(Review r);
    Long getMaxStopOfRoutes();
    List<Route> getRoutsNotSell();
    List<Route> getRoutesWithStop(Stop stop);
    List<User> getTop5UsersMorePurchases();
    long getCountOfPurchasesBetweenDates(Date start, Date end);
}
