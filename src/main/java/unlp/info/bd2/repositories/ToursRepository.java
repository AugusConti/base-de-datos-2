package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.User;
import unlp.info.bd2.utils.ToursException;

public interface ToursRepository {
    void save(Object o);
    <T> Optional<T> findById(long id, Class<T> _class);
    void update(Object o);
    void delete(Object o);
    <T> List<T> findManyByAtribute(Class<T> resultClass, String atributeName, String atributeValue);
    List<Route> getRoutesBelowPrice(float price);
    }
