package unlp.info.bd2.repositories; 
 
 

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
 
import unlp.info.bd2.model.Review; 
 

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
 
    
}
