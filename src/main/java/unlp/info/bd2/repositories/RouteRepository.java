package unlp.info.bd2.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import unlp.info.bd2.model.Route;

import java.util.List;

public interface RouteRepository extends CrudRepository<Route, Long> {
    List<Route> findByPriceLessThan(float price);

    @Query("SELECT rte FROM Review rev JOIN rev.purchase p JOIN p.route rte GROUP BY rte.id ORDER BY AVG(rev.rating) DESC")
    List<Route> findAllSortByAverageRatingDesc(Pageable pageable);

    @Query("FROM Route r LEFT JOIN Purchase p ON p.route = r WHERE p.id IS NULL")
    List<Route> findNotSell();

    @Query("SELECT DISTINCT rte FROM Review rev JOIN rev.purchase p JOIN p.route rte WHERE rev.rating = 1")
    List<Route> findWithMinRating();
}
