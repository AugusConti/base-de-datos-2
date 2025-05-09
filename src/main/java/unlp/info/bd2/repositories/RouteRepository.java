package unlp.info.bd2.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

import java.util.List;

public interface RouteRepository extends CrudRepository<Route, Long> {
    List<Route> findByPriceLessThan(float price);

    @Query("SELECT rte FROM Review rev JOIN rev.purchase p JOIN p.route rte GROUP BY rte.id ORDER BY AVG(rev.rating) DESC")
    List<Route> findAllSortByAverageRatingDesc(Pageable pageable);

    @Query("FROM Route r LEFT JOIN Purchase p ON p.route = r WHERE p.id IS NULL")
    List<Route> findNotSell();

    @Query("SELECT DISTINCT rte FROM Review rev JOIN rev.purchase p JOIN p.route rte WHERE rev.rating = 1")
    List<Route> findWithMinRating();

    @Query("FROM Route r ORDER BY SIZE(r.stops) DESC")
    List<Route> findTop3RoutesWithMoreStops(Pageable pageable);

    @Query("SELECT MAX(SIZE(r.stops)) FROM Route r")
    Long findMaxStopOfRoutes();

    List<Route> findByStopsContaining(Stop stop);
}
