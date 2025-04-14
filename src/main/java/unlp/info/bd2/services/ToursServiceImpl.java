package unlp.info.bd2.services;

import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.User;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ToursServiceImpl implements ToursService{
    ToursRepository repository;
    
    public ToursServiceImpl(ToursRepository tr){
        this.repository = tr;
    }
    public User createUser(String username, String password, String fullName, String email, Date birthdate, String phoneNumber) throws ToursException {
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setName(fullName);
        u.setEmail(email);
        u.setBirthdate(birthdate);
        u.setPhoneNumber(phoneNumber);
        this.repository.save(u);
        return u;
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
}
