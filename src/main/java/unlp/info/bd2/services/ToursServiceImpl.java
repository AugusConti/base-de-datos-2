package unlp.info.bd2.services;

import unlp.info.bd2.model.*;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@org.springframework.stereotype.Service
public class ToursServiceImpl implements ToursService{
    ToursRepository repository;
    
    public ToursServiceImpl(ToursRepository tr){
        this.repository = tr;
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        if (this.repository.findUserByUsername(username).isPresent()){
            throw new ToursException("Ya existe un usuario con ese nombre, por favor ingrese otro");
        }
        else{
            User u = new User(username, password, fullName, email, birthdate, phoneNumber);
            this.repository.save(u);
            return u;
        }
    }

    @Override
    @Transactional
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        if (this.repository.findUserByUsername(username).isPresent()){
            throw new ToursException("Ya existe un usuario con ese nombre, por favor ingrese otro");
        }
        else{
            DriverUser d = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
            this.repository.save(d);
            return d;
        }
    }

    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
        if (this.repository.findUserByUsername(username).isPresent()){
            throw new ToursException("Ya existe un usuario con ese nombre, por favor ingrese otro");
        }
        else{
            TourGuideUser t = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
            this.repository.save(t);
            return t;
        }
    }

    @Override
    public Optional<User> getUserById(Long id) throws ToursException {
        try {
            return this.repository.findById(id, User.class);
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        try {
            return this.repository.findUserByUsername(username);
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public User updateUser(User user) throws ToursException {
        try {
            this.repository.update(user);
            return this.repository.findById(user.getId(), user.getClass()).get();
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Stop createStop(String name, String description) throws ToursException{
        Stop s = new Stop(name, description);
        this.repository.save(s);
        return s;
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        List<Stop> result = this.repository.findManyByAtribute(Stop.class,"name",name);
        return result;
    }

    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{
        if (price < 0 || totalKm < 0 || maxNumberOfUsers < 0){
            throw new ToursException("El valor del precio, los kilómetros o la cantidad máxima de usuarios no puede ser negativos");
        }
        else{
            Route r = new Route(name, price, totalKm, maxNumberOfUsers, stops);
            this.repository.save(r);
            return r;
        }
    }

    @Override
    public Optional<Route> getRouteById(Long id){
        return this.repository.findById(id, Route.class);
    }
    
    @Override
    public List<Route> getRoutesBelowPrice(float price){
        return this.repository.getRoutesBelowPrice(price);
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        if(this.repository.existeSupplier(authorizationNumber)){
            throw new ToursException("Ya existe un supplier con authorization number: " + authorizationNumber);
        }
        else{
            Supplier s = new Supplier(businessName,authorizationNumber);
            this.repository.save(s);
            return s;
        }
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{
        Service s = new Service(name, price, description, supplier);
        supplier.addService(s);
        this.repository.save(s);
        return s;
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id){
        return this.repository.findById(id, Supplier.class);
    }

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber){
        return this.repository.findOneByAtribute(Supplier.class, "authorizationNumber", authorizationNumber);
    }

    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException{
        return this.repository.getServiceByNameAndSupplierId(name, id);
    }

    @Override
    @Transactional
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException{
        Optional<Service> s = this.repository.findById(id,Service.class);
        if(s.isEmpty()){
            throw new ToursException("No existe un servicio con ID: " + id);
        }
        else{
            s.get().setPrice(newPrice);
            this.repository.update(s.get());
        }
        return s.get();
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        return this.repository.getTopNSuppliersInPurchases(n);
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        return this.repository.getUserSpendingMoreThan(mount);
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        return this.repository.getTop3RoutesWithMaxRating();
    }

    @Override
    public Service getMostDemandedService() {
        return this.repository.getMostDemandedService();
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return this.repository.getTourGuidesWithRating1();
    }

    @Override
    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){
        return this.repository.getTop10MoreExpensivePurchasesInServices();
    } 
 //
    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route,User user)throws ToursException{
        Optional<User> u = this.repository.findById(user.getId(),User.class);
        if(u.isEmpty()){ 
            throw new ToursException("No existe un usuario con ID: " + user.getId());
        }
        Optional<Route> r = this.repository.findById(route.getId(),Route.class);
        if(r.isEmpty()){
            throw new ToursException("No existe una ruta con ID: " + route.getId());
        } 
        if (!this.repository.findManyByAtribute( Purchase.class,"code",code).isEmpty()) {
            throw new ToursException("Ya existe una compra con code: " + code);
        }
        // falta verificar la disponibilidad de la ruta en la fecha.
        Purchase p = new Purchase(code, date, user, route);
        if(!this.repository.canCreatePurchase(p)){
            throw new ToursException("No hay mas cupos para la ruta con ID "+ p.getRoute().getId());
        }
        user.addPurchase(p);
        this.repository.save(p);
        return p;
    }

    @Override
    @Transactional
    public Purchase createPurchase(String code,Route route,User user)throws ToursException{
        Purchase p = createPurchase(code, new Date(), route, user);
        return p; 
    } 

    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException{
        ItemService i= new ItemService(quantity, purchase, service); 
        this.repository.save(i);
        return i;
    }
     
    @Override
    public Optional<Purchase> getPurchaseByCode(String code){//revisar si no existe ninguno
        return this.repository.findOneByAtribute(Purchase.class, "code", code);
    }

    @Override
    @Transactional 
    public void deletePurchase(Purchase purchase) throws ToursException{
        //borra la compra y ver que se borre de la lista de compras del usuario
        // ya con la cascada se sabe que se borra del usuario ?
        this.repository.delete(purchase);
    }

    @Override
    @Transactional 
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException{
        Review r = purchase.addReview(rating,comment);
        this.repository.save(r);
        return r;
    }

    @Override
    public Long getMaxStopOfRoutes(){
        return this.repository.getMaxStopOfRoutes();
    }

    @Override
    public List<Route> getRoutsNotSell(){
        return this.repository.getRoutsNotSell();
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop){
        return this.repository.getRoutesWithStop(stop);
    }

    @Override
    @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        try{
            Route r = this.repository.findById(idRoute, Route.class).get();
            DriverUser d = this.repository.findDriverByUsername(username).get();
            r.addDriver(d);
            d.addRoute(r);
            this.repository.save(d);
        }
        catch (Exception e){
            throw new ToursException("No se encontró el driver o la ruta");
        }
    }

    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{
        try{
            Route r = this.repository.findById(idRoute, Route.class).get();
            TourGuideUser t = this.repository.findTourGuideByUsername(username).get();
            r.addTourGuide(t);
            t.addRoute(r);
            this.repository.save(t);
        }
        catch (Exception e){
            throw new ToursException("No se encontró el tour guide o la ruta");
        }
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException{
        if(!user.isActive()){
            throw new ToursException("Ese usuario ya fue borrado");
        }
        else{
            if(!user.canBeDesactive()){
                throw new ToursException("Este usuario no puede ser borrado");
            }
            else{
                if(user.canBeDeleted()){
                    this.repository.delete(user);
                }
                else{
                    user.setActive(false);
                    this.repository.save(user);
                }
            }
        }
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username){
        return this.repository.findUserByUsername(username).get().getPurchaseList();
    }

    @Override
    public  List<User> getTop5UsersMorePurchases(){
        return this.repository.getTop5UsersMorePurchases();
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end){
        return this.repository.getCountOfPurchasesBetweenDates(start, end);

    }
    
    @Override
    public List<Service> getServiceNoAddedToPurchases(){
        return this.repository.getServiceNoAddedToPurchases();
    }

}
