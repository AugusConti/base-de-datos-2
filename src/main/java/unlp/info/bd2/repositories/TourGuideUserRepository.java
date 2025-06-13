package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Query;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import unlp.info.bd2.model.TourGuideUser;

public interface TourGuideUserRepository extends MongoRepository<TourGuideUser, ObjectId> {
    
    Optional<TourGuideUser> findByUsername(String username);

}