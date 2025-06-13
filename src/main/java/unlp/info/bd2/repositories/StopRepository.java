package unlp.info.bd2.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import unlp.info.bd2.model.Stop;

import java.util.List;

public interface StopRepository extends MongoRepository<Stop, ObjectId>{

    List<Stop> findByNameStartingWith(String name);

}