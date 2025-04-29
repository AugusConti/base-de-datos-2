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
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setName(fullName);
        u.setEmail(email);
        u.setBirthdate(birthdate);
        u.setPhoneNumber(phoneNumber);
        u.setPurchases(new ArrayList<Purchase>());
        u.setActive(true);
        this.repository.createUser(u);
        return u;
    }

    @Override
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
            du.setRoutes(new ArrayList<Route>());
            du.setActive(true);
            this.repository.save(du);
            return du;
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Override
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
            tgu.setRoutes(new ArrayList<Route>());
            this.repository.save(tgu);
            tgu.setActive(true);
            return tgu;
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
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
    public User updateUser(User user) throws ToursException {
        try {
            this.repository.update(user);
            return this.repository.findById(user.getId(), user.getClass()).get();
        } catch (Exception e) {
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException{
        Stop s = new Stop();
        s.setName(name);
        s.setDescription(description);
        s.setRoutes(new ArrayList<Route>());
        this.repository.save(s);
        return s;
    }

    @Override
    public List<Stop> getStopByNameStart(String name) {
        List<Stop> result = this.repository.findManyByAtribute(Stop.class,"name",name);
        return result;
    }

    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops) throws ToursException{
        Route r = new Route();
        r.setName(name);
        r.setPrice(price);
        r.setTotalKm(totalKm);
        r.setMaxNumberUsers(maxNumberOfUsers);
        r.setStops(stops);
        r.setDrivers(new ArrayList<DriverUser>());
        r.setTourGuides(new ArrayList<TourGuideUser>());
        this.repository.save(r);
        return r;
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
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException{
        Supplier s = new Supplier();
        s.setBusinessName(businessName);
        s.setAuthorizationNumber(authorizationNumber);
        s.setServices(new ArrayList<Service>());
        this.repository.createSupplier(s);
        return s;
    }

    @Override
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException{
        Service s = new Service();
        s.setName(name);
        s.setPrice(price);
        s.setDescription(description);
        s.setSupplier(supplier);
        s.setItemServices(new ArrayList<ItemService>());
        this.repository.addServiceToSupplier(s, supplier);
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
    public Purchase createPurchase(String code, Date date, Route route,User user)throws ToursException{
        Optional<User> u = this.repository.findById(user.getId(),User.class); 
        if(u.isEmpty()){//necesario?
            throw new ToursException("No existe un usuario con ID: " + user.getId());
        }
        Optional<Route> r = this.repository.findById(route.getId(),Route.class);
        if(r.isEmpty()  ){//necesario?
            throw new ToursException("No existe una ruta con ID: " + route.getId());
        } 
        if (!this.repository.findManyByAtribute( Purchase.class,"code",code).isEmpty()) {
            throw new ToursException("Ya existe una compra con code: " + code);
        }
        
        Purchase p = new Purchase();//revisar
         p.setCode(code);
         p.setDate(date);
         p.setRoute(route);
         p.setUser(user); 
         p.setItemServiceList((new ArrayList<ItemService>())); 
         p.setTotalPrice(route.getPrice());  
         this.repository.createPurchase(p);
         return p; 
    }

    @Override
    public Purchase createPurchase(String code,Route route,User user)throws ToursException{
        Purchase p = createPurchase(code,  new Date() ,route, user);
        return p; 
    } 

    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException{
        Optional<Purchase> p = this.repository.findById(purchase.getId(),Purchase.class);
        if(p.isEmpty()){//necesario?
            throw new ToursException("No existe una compra con ID: " + purchase.getId());
        }
        else{
            ItemService i= new ItemService();//revisar la exception 
            i.setPurchase(purchase);
            i.setQuantity(quantity);
            i.setService(service); 
            float totalPrice= p.get().getTotalPrice() + i.getQuantity()* i.getService().getPrice();
            this.repository.addItemToPurchase(i, totalPrice, purchase);
            return i;
        }
    }
     
    @Override
    public Optional<Purchase> getPurchaseByCode(String code){//revisar si no existe ninguno
        return this.repository.findOneByAtribute(Purchase.class, "code", code);

    }

    @Override
    @Transactional //aca o en service 
    public void deletePurchase(Purchase purchase) throws ToursException{//Revisar
        //borra la compra y ver que se borre de la lista de compras del usuario 
        Optional<Purchase> p = this.repository.findById(purchase.getId(),Purchase.class);
        if(p.isEmpty()){//es necesaio?
            throw new ToursException("No existe una compra con ID: " + purchase.getId());
        }else{
            User u= purchase.getUser();
            u.getPurchaseList().remove(purchase);//modificar una collection ajena?
            this.repository.update(u);
            this.repository.delete(purchase);//delete purchase y en la lista
            // o se le agrega el orphan en user las purchases
            //yse borra de la lista 
        }
    }

    @Override
    @Transactional //aca o en repo?
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException{
        Optional<Purchase> p = this.repository.findById(purchase.getId(),Purchase.class);
        if(p.isEmpty()){
            throw new ToursException("No existe una compra con ID: " + purchase.getId());
        }
        else{
        Review r= new Review();//revisar que el purchase exista?
        r.setPurchase(purchase);
        r.setComment(comment);
        r.setRating(rating);
        this.repository.save(r);
        this.repository.addReviewToPurchase(r);
        return r;
        }

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
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        //Consultar esto
        try{
            Optional<Route> ro = this.repository.findById(idRoute, Route.class);
            Optional<User> u = this.repository.findUserByUsername(username);
            DriverUser d = (DriverUser) u.get();
            Route r = ro.get();
            r.addDriver(d);
            d.addRoute(r);
            this.repository.setDriverToRoute(d,r);
        }
        catch (Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException{
        //Consultar PUEDO HACER DOS UPDATES Y PONERLE TRANSACTIONAL?
        try{
            Optional<Route> ro = this.repository.findById(idRoute, Route.class);
            Optional<User> u = this.repository.findUserByUsername(username);
            TourGuideUser t = (TourGuideUser) u.get();
            Route r = ro.get();
            r.addTourGuide(t);
            t.addRoute(r);
            this.repository.setTourGuideToRoute(t,r);
        }
        catch (Exception e){
            throw new ToursException(e.getMessage());
        }
    }

    @Override
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
