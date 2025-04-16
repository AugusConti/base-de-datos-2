package unlp.info.bd2.services;

import unlp.info.bd2.model.*;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

public class ToursServiceImpl implements ToursService{
    ToursRepository repository;
    
    public ToursServiceImpl(ToursRepository tr){
        this.repository = tr;
    }
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        try {
            User u = new User();
            u.setUsername(username);
            u.setPassword(password);
            u.setName(fullName);
            u.setEmail(email);
            u.setBirthdate(birthdate);
            u.setPhoneNumber(phoneNumber);
            u.setPurchases(new ArrayList<Purchase>());
            this.repository.save(u);
            return u;
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        try {
            DriverUser du = new DriverUser();
            du.setUsername(username);
            du.setPassword(password);
            du.setName(fullName);
            du.setEmail(email);
            du.setBirthdate(birthdate);
            du.setPhoneNumber(phoneNumber);
            du.setExpedient(expedient);
            this.repository.save(du);
            return du;
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
        try {
            TourGuideUser tgu = new TourGuideUser();
            tgu.setUsername(username);
            tgu.setPassword(password);
            tgu.setName(fullName);
            tgu.setEmail(email);
            tgu.setBirthdate(birthdate);
            tgu.setPhoneNumber(phoneNumber);
            tgu.setEducation(education);
            this.repository.save(tgu);
            return tgu;
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    public Optional<User> getUserById(Long id) throws ToursException {
        try {
            return this.repository.findById(id, User.class);
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    public Optional<User> getUserByUsername(String username) throws ToursException {
        try {
            return this.repository.findUserByUsername(username);
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    public User updateUser(User user) throws ToursException {
        try {
            this.repository.update(user);
            return this.repository.findById(user.getId(), user.getClass()).get();
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }
    public Stop createStop(String name, String description) throws ToursException{
        Stop s = new Stop();
        s.setName(name);
        s.setDescription(description);
        s.setRoutes(new ArrayList<Route>());
        this.repository.save(s);
        return s;
    }
    public List<Stop> getStopByNameStart(String name) {
        List<Stop> result = this.repository.findManyByAtribute(Stop.class,"name",name);
        return result;
    }
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{
        Route r = new Route();
        r.setName(name);
        r.setPrice(price);
        r.setTotalKm(totalKm);
        r.setMaxNumberUsers(maxNumberOfUsers);
        r.setStops(stops);
        this.repository.save(r);
        return r;
    }
    public Optional<Route> getRouteById(Long id){
        return this.repository.findById(id, Route.class);
    }
    public List<Route> getRoutesBelowPrice(float price){
        return this.repository.getRoutesBelowPrice(price);
    }
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        Supplier s = new Supplier();
        s.setBusinessName(businessName);
        s.setAuthorizationNumber(authorizationNumber);
        s.setServices(new ArrayList<Service>());
        this.repository.createSupplier(s);
        return s;
    }
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{
        Service s = new Service();
        s.setName(name);
        s.setPrice(price);
        s.setDescription(description);
        s.setSupplier(supplier);
        this.repository.addServiceToSupplier(s, supplier);
        return s;
    }
    public Optional<Supplier> getSupplierById(Long id){
        return this.repository.findById(id, Supplier.class);
    }
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber){
        return this.repository.findOneByAtribute(Supplier.class, "authorizationNumber", authorizationNumber);
    }
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException{
        return this.repository.getServiceByNameAndSupplierId(name, id);
    }
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
    
    public List<Supplier> getTopNSuppliersInPurchases(int n){
        return this.repository.getTopNSuppliersInPurchases(n);
    }
    public List<Purchase> getTop10MoreExpensivePurchasesInServices(){
        return this.repository.getTop10MoreExpensivePurchasesInServices();
    }
 
    public Purchase createPurchase(String code, Date date, Route route,User user)throws ToursException{
         Purchase p = new Purchase();//revisar
         p.setCode(code);
         p.setDate(date);
         p.setRoute(route);
         p.setUser(user); 
         p.setItemServiceList((new ArrayList<ItemService>())); 
         p.setTotalPrice(route.getPrice()); 
         System.out.println("EEEEEEEEEEEE:"+p.toString());
         this.repository.createPurchase(p);
         return p; 
    }

    public Purchase createPurchase(String code,Route route,User user)throws ToursException{
        Purchase p = createPurchase(code,  new Date() ,route, user);
        return p;
}
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException{
        ItemService i= new ItemService();//revisar la exception 
        i.setPurchase(purchase);
        i.setQuantity(quantity);
        i.setService(service);
        this.repository.addItemToPurchase(i);
        return i;
    }

    public Optional<Purchase> getPurchaseByCode(String code){//revisar
        return this.repository.findOneByAtribute(Purchase.class, "code", code);
   
    }

    public void deletePurchase(Purchase purchase) throws ToursException{
        //borra la compra y ver que se borre de la lista de compras del usuario 
        try{
        this.repository.delete(purchase);
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException{
        Review r= new Review();//revisar que el purchase exista?
        r.setPurchase(purchase);
        r.setComment(comment);
        r.setRating(rating);
        this.repository.addReviewToPurchase(r);
        return r;

    }
    
}
