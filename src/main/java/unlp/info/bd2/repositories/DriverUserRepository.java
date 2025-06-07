package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import unlp.info.bd2.model.DriverUser;

public interface DriverUserRepository extends MongoRepository<DriverUser, ObjectId> {
    
    Optional<DriverUser> findByUsername(String username);

    @Query("FROM DriverUser du ORDER BY SIZE(du.routes) DESC")
    List<DriverUser> findAllSortByRouteCountDesc(Pageable pageable);

}