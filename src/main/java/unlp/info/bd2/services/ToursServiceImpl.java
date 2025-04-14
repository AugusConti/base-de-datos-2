package unlp.info.bd2.services;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

import java.util.Date;
import java.util.Optional;

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
}
