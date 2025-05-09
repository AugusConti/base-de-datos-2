package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.*;
import unlp.info.bd2.utils.ToursException;

public interface ToursRepository {
    void save(Object o) throws ToursException;
    <T> Optional<T> findById(long id, Class<T> _class);
    <T> T update(T o) throws ToursException;
    void delete(Object o) throws ToursException;

    Optional<User> findUserByUsername(String username);
    Optional<TourGuideUser> findTourGuideByUsername(String username);
    Optional<DriverUser> findDriverByUsername(String username);
    List<Stop> findStopsByNameStart(String name);
    List<Route> getRoutesBelowPrice(float price);
    Optional<Service> getServiceByNameAndSupplierId(String name, Long id);
    Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber);
    List<Supplier> getTopNSuppliersInPurchases(int n);
    List<User> getUserSpendingMoreThan(float mount);
    List<Route> getTop3RoutesWithMaxRating();
    Service getMostDemandedService();
    List<TourGuideUser> getTourGuidesWithRating1();
    List<Purchase> getTop10MoreExpensivePurchasesInServices(); 
    Optional<Purchase> getPurchaseByCode(String code);
    int countPurchasesByDateAndRoute(Date date, Route route);
    Long getMaxStopOfRoutes();
    List<Route> getRoutsNotSell();
    List<Route> getRoutesWithStop(Stop stop);
    List<User> getTop5UsersMorePurchases();
    long getCountOfPurchasesBetweenDates(Date start, Date end);
    List<Service> getServiceNoAddedToPurchases();
}
