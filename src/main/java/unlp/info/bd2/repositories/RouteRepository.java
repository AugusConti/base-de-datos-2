package unlp.info.bd2.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

import java.util.List;

public interface RouteRepository extends MongoRepository<Route, ObjectId> {
    List<Route> findByPriceLessThan(float price);

    //TODO CONSULTAR SI PODEMOS USAR UN @QUERY O SI O SI HAY QUE USAR @AGGREGATION, XQ ESTO ANDA
    @Query("SELECT rte FROM Review rev JOIN rev.purchase p JOIN p.route rte GROUP BY rte.id ORDER BY AVG(rev.rating) DESC")
    List<Route> findAllSortByAverageRatingDesc(Pageable pageable);

    @Query("SELECT rte FROM Purchase p JOIN p.route rte GROUP BY rte.id ORDER BY COUNT(p) DESC")
    List<Route> findMostPurchasedRoutes(Pageable pageable);

    @Query("FROM Route r LEFT JOIN Purchase p ON p.route = r WHERE p.id IS NULL")
    List<Route> findNotSell();

    @Query("SELECT DISTINCT rte FROM Review rev JOIN rev.purchase p JOIN p.route rte WHERE rev.rating = 1")
    List<Route> findWithMinRating();

    @Query("FROM Route r ORDER BY SIZE(r.stops) DESC")
    List<Route> findTop3RoutesWithMoreStops(Pageable pageable);

    @Aggregation(pipeline = {
            "{ $project: { stopsSize: { $size: '$stops' } } }",
            "{ $group:   { _id: null, maxStops: { $max: '$stopsSize' } } }"
    })
    Long findMaxStopOfRoutes();

    List<Route> findByStopsContaining(Stop stop);
}