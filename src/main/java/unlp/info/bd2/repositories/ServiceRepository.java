package unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Service;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {

    @Query("FROM Service s JOIN s.itemServiceList item ORDER BY SUM(item.quantity) DESC")
    List<Service> findAllSortByItemQuantitySumDesc(Pageable pageable);

}
