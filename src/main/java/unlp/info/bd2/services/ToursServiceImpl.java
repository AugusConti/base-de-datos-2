package unlp.info.bd2.services;

import java.util.Date;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import unlp.info.bd2.model.*;
import unlp.info.bd2.repositories.*;
import unlp.info.bd2.utils.ToursException;

public class ToursServiceImpl implements ToursService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverUserRepository driverUserRepository;

    @Autowired
    private TourGuideUserRepository tourGuideUserRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ItemServiceRepository itemServiceRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // TODO CONSULTAR: ¿Por qué no se verifica al guardar el usuario?
    private void assertUniqueUsername(String username) throws ToursException {
        if (this.userRepository.existsByUsername(username))
            throw new ToursException("Username already assigned");
    }

    @Transactional
    @Override
    public User createUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber) throws ToursException {
        try {
            this.assertUniqueUsername(username);
            User user = new User(username, password, fullName, email, birthdate, phoneNumber);
            return this.userRepository.save(user);
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
            return this.driverUserRepository.save(driverUser);
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
            return this.tourGuideUserRepository.save(tourGuideUser);
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        return this.userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return this.userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public User updateUser(User user) throws ToursException {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(User user) throws ToursException {
        if (!user.canBeDesactive())
            throw new ToursException("No se puede desactivar el usuario");
        if (!user.isActive())
            throw new ToursException("El usuario ya fue desactivado");
        if (user.canBeDeleted())
            this.userRepository.delete(user);
        else
            user.setActive(false);
    }

    @Override
    @Transactional
    public Stop createStop(String name, String description) throws ToursException {
        //TODO CONSULTAR TEMA DE LAS EXCEPCIONES
        Stop s = new Stop(name, description);
        this.stopRepository.save(s);
        return s;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return this.stopRepository.findByNameStartingWith(name);
    }

    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException {
        Route r = new Route(name, price, totalKm, maxNumberOfUsers, stops);
        this.routeRepository.save(r);
        return r;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return this.routeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price) {
        return this.routeRepository.findByPriceLessThan(price);
    }

    @Override
    @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        try{
            DriverUser d = this.driverUserRepository.findByUsername(username).get();
            Route r = this.routeRepository.findById(idRoute).get();
            d.addRoute(r);
            r.addDriver(d);
            this.userRepository.save(d);
        }catch (Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        try{
            TourGuideUser t = this.tourGuideUserRepository.findByUsername(username).get();
            Route r = this.routeRepository.findById(idRoute).get();
            t.addRoute(r);
            r.addTourGuide(t);
            this.userRepository.save(t);
        } catch (Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        try{
            Supplier s = new Supplier(businessName, authorizationNumber);
            this.supplierRepository.save(s);
            this.entityManager.flush();
            return s;
        }
        catch (Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException {
        Service s = new Service(name, price, description, supplier);
        supplier.addService(s);
        this.serviceRepository.save(s);
        return s;
    }

    @Transactional
    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        try {
            Service s = this.serviceRepository.findById(id).get();
            s.setPrice(newPrice);
            this.serviceRepository.save(s);
            return s;
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        return this.supplierRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return this.supplierRepository.findByAuthorizationNumber(authorizationNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return this.serviceRepository.findByNameAndSupplierId(name, id);
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        Purchase p = createPurchase(code, new Date() , route, user);
        return p; 
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        long cant=this.purchaseRepository.countByRouteAndDate(route, date);
        if(cant >= route.getMaxNumberUsers()){
            throw new ToursException("No hay mas cupos para la ruta con ID "+ route.getId());
        }

        if (this.purchaseRepository.findByCode(code).isPresent()) {
           throw new ToursException("El code "+ code+" ya es utilizado para una purchase");
        }
         
        try{
            Purchase p = new Purchase(code, date, user, route);
            user.addPurchase(p);
            this.purchaseRepository.save(p);            
            return p;
        
        }  
        catch (Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService i= new ItemService(quantity, purchase, service);  
        this.itemServiceRepository.save(i);
        return i;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Purchase> getPurchaseByCode(String code) { 
        return this.purchaseRepository.findByCode(code);
    }

    @Override
    @Transactional
    public void deletePurchase(Purchase purchase) throws ToursException {
        purchase.getUser().removePurchase(purchase);
        this.purchaseRepository.delete(purchase);    
    }

    @Override
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        Review r = purchase.addReview(rating,comment);
        this.reviewRepository.save(r);
        return r;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return this.purchaseRepository.findAllByUserUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        return userRepository.findByPurchaseListTotalPriceGreaterThanEqual(mount);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getUsersWithNumberOfPurchases(int number) {
        return userRepository.findByPurchaseCountGreaterThan(number);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return this.supplierRepository.findTopNSuppliersInPurchases(PageRequest.ofSize(n));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Supplier> getTopNSuppliersItemsSold(int n) {
       return this.supplierRepository.findTopNSuppliersItemsSold(PageRequest.ofSize(n));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Purchase> getTop10MoreExpensivePurchasesWithServices() {
        return this.purchaseRepository.findByItemServiceListIsNotEmptyOrderByTotalPriceDesc(PageRequest.ofSize(10));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getTop5UsersMorePurchases() {
        return userRepository.findAllSortByPurchaseCountDesc(PageRequest.ofSize(5));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Route> getTop3RoutesWithMoreStops() {
        return this.routeRepository.findTop3RoutesWithMoreStops(PageRequest.ofSize(3));
    }

    @Transactional(readOnly = true)
    @Override
    public Long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return this.purchaseRepository.countByDateBetween(start, end);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return this.routeRepository.findByStopsContaining(stop);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Purchase> getPurchaseWithService(Service service) {
        return this.purchaseRepository.findAllByItemServiceListService(service); 
    }

    @Transactional(readOnly = true)
    @Override
    public Long getMaxStopOfRoutes() {
        return this.routeRepository.findMaxStopOfRoutes();
    }

    @Transactional(readOnly = true)
    @Override
    public Long getMaxServicesOfSupplier() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxServicesOfSupplier'");
    }

    @Transactional(readOnly = true)
    @Override
    public List<Route> getRoutsNotSell() {
        return this.routeRepository.findNotSell();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Route> getTop3RoutesWithMaxAverageRating() {
        return routeRepository.findAllSortByAverageRatingDesc(PageRequest.ofSize(3));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Route> getRoutesWithMinRating() {
        return this.routeRepository.findWithMinRating();
    }

    @Transactional(readOnly = true)
    @Override
    public Service getMostDemandedService() {
        return serviceRepository.findAllSortByItemQuantitySumDesc(PageRequest.ofSize(1)).get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public Route getMostBestSellingRoute() {
        return this.routeRepository.findMostPurchasedRoutes(PageRequest.of(0, 1)).get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Service> getServiceNoAddedToPurchases() {
        return this.serviceRepository.getServiceNoAddedToPurchases();
        }

    @Transactional(readOnly = true)
    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return this.tourGuideUserRepository.findByRating(1);
    }

    @Transactional(readOnly = true)
    @Override
    public DriverUser getDriverUserWithMoreRoutes() {
        return this.driverUserRepository.findAllSortByRouteCountDesc(PageRequest.ofSize(1)).get(0);
    }
}
