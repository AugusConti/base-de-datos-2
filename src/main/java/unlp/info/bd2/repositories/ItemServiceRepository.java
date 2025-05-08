package unlp.info.bd2.repositories; 
 
 

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.ItemService; 
 

@Repository
public interface ItemServiceRepository extends CrudRepository<ItemService, Long> {
 
    
}
