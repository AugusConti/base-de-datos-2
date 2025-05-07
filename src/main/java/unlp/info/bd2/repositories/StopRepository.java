package unlp.info.bd2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import unlp.info.bd2.model.Stop;

import java.util.List;

public interface StopRepository extends JpaRepository<Stop, Long>{
    List<Stop> findByNameStartingWith(String name);
}
