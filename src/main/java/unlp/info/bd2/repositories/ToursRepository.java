package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.Optional;

import unlp.info.bd2.model.User;
import unlp.info.bd2.utils.ToursException;

public interface ToursRepository {
    void save(Object o);
    <T> Optional<T> findById(long id, Class<T> _class);
    void update(Object o);
    void delete(Object o);

    Optional<User> findUserByUsername(String username);
}
