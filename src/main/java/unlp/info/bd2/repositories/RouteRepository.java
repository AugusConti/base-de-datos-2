package unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.Route;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {

    @Query("SELECT rte FROM Review rev JOIN rev.purchase p JOIN p.route rte GROUP BY rte.id ORDER BY AVG(rev.rating) DESC")
    List<Route> findAllSortByAverageRatingDesc(Pageable pageable);

}
