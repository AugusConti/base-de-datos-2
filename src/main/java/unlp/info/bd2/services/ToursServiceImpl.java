package unlp.info.bd2.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.repositories.*;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService {

    @Autowired
    RouteRepository routeRepository;
    
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    UserRepository userRepository;

    // TODO CONSULTAR: ¿Por qué no se verifica al guardar el usuario?
    private void assertUniqueUsername(String username) throws ToursException {
        if (userRepository.existsByUsername(username))
            throw new ToursException("Username already assigned");
    }

    @Transactional
    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber) throws ToursException {
        try {
            this.assertUniqueUsername(username);
            User user = new User(username, password, fullName, email, birthdate, phoneNumber);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        try {
            this.assertUniqueUsername(username);
            DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
            return userRepository.save(driverUser);
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
        try {
            this.assertUniqueUsername(username);
            TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
            return userRepository.save(tourGuideUser);
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(User user) throws ToursException {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        userRepository.delete(user);
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createStop'");
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStopByNameStart'");
    }

    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops)
            throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createRoute'");
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRouteById'");
    }

    @Override
    public List<Route> getRoutesBelowPrice(float price) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoutesBelowPrice'");
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignDriverByUsername'");
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignTourGuideByUsername'");
    }

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSupplier'");
    }

    @Override
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier)
            throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addServiceToSupplier'");
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateServicePriceById'");
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSupplierById'");
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSupplierByAuthorizationNumber'");
    }

    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServiceByNameAndSupplierId'");
    }

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPurchase'");
    }

    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPurchase'");
    }

    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addItemToPurchase'");
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPurchaseByCode'");
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePurchase'");
    }

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addReviewToPurchase'");
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllPurchasesOfUsername'");
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        return userRepository.findByMountSpendingGreaterThan(mount);
    }

    @Override
    public List<User> getUsersWithNumberOfPurchases(int number) {
        return userRepository.findByPurchaseCountGreaterThan(number);
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopNSuppliersInPurchases'");
    }

    @Override
    public List<Supplier> getTopNSuppliersItemsSold(int n) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopNSuppliersItemsSold'");
    }

    @Override
    public List<Purchase> getTop10MoreExpensivePurchasesWithServices() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTop10MoreExpensivePurchasesWithServices'");
    }

    @Override
    public List<User> getTop5UsersMorePurchases() {
        return userRepository.findAllSortByPurchaseCountDesc(PageRequest.ofSize(5));
    }

    @Override
    public List<Route> getTop3RoutesWithMoreStops() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTop3RoutesWithMoreStops'");
    }

    @Override
    public Long getCountOfPurchasesBetweenDates(Date start, Date end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCountOfPurchasesBetweenDates'");
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoutesWithStop'");
    }

    @Override
    public List<Purchase> getPurchaseWithService(Service service) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPurchaseWithService'");
    }

    @Override
    public Long getMaxStopOfRoutes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxStopOfRoutes'");
    }

    @Override
    public Long getMaxServicesOfSupplier() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxServicesOfSupplier'");
    }

    @Override
    public List<Route> getRoutsNotSell() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoutsNotSell'");
    }

    @Override
    public List<Route> getTop3RoutesWithMaxAverageRating() {
        return routeRepository.findAllSortByAverageRatingDesc(PageRequest.ofSize(3));
    }

    @Override
    public List<Route> getRoutesWithMinRating() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoutesWithMinRating'");
    }

    @Override
    public Service getMostDemandedService() {
        return serviceRepository.findAllSortByItemQuantitySumDesc(PageRequest.ofSize(1)).getFirst();
    }

    @Override
    public Route getMostBestSellingRoute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMostBestSellingRoute'");
    }

    @Override
    public List<Service> getServiceNoAddedToPurchases() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServiceNoAddedToPurchases'");
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return userRepository.findTourGuidesByRating(1);
    }

    @Override
    public DriverUser getDriverUserWithMoreRoutes() {
        return userRepository.findDriversSortByRouteCountDesc(PageRequest.ofSize(1)).getFirst();
    }
}
