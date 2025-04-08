package unlp.info.bd2.services;

import unlp.info.bd2.model.User;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

import java.util.Date;

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
        return u;
    }
}
