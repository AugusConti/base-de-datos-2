package unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import unlp.info.bd2.model.DriverUser;

public interface DriverUserRepository extends BaseUserRepository<DriverUser> {

    @Query("FROM DriverUser du ORDER BY COUNT(du.routes) DESC")
    List<DriverUser> findAllSortByRouteCountDesc(Pageable pageable);

}
