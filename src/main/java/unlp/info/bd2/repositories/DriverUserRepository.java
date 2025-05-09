package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import unlp.info.bd2.model.DriverUser;

public interface DriverUserRepository extends CrudRepository<DriverUser, Long> {
    
    Optional<DriverUser> findByUsername(String username);

    @Query("FROM DriverUser du ORDER BY SIZE(du.routes) DESC")
    List<DriverUser> findAllSortByRouteCountDesc(Pageable pageable);

}
