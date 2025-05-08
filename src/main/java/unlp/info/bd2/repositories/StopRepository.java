package unlp.info.bd2.repositories;

import org.springframework.data.repository.CrudRepository;
import unlp.info.bd2.model.Stop;

import java.util.List;

public interface StopRepository extends CrudRepository<Stop, Long>{
    List<Stop> findByNameStartingWith(String name);
}
