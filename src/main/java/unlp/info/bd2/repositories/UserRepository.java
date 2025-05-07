package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT DISTINCT u FROM User u JOIN u.purchaseList p WHERE p.totalPrice >= :mount")
    List<User> findByMountSpendingGreaterThan(@Param("mount") float mount);

    @Query("SELECT u FROM User u WHERE COUNT(u.purchaseList) >= :number")
    List<User> findByPurchaseCountGreaterThan(@Param("number") int number);

    @Query("SELECT u FROM User u ORDER BY COUNT(u.purchaseList) DESC")
    List<User> findAllSortByPurchaseCountDesc(Pageable pageable);

    // TODO CONSULTAR: ¿Repo único o uno por tipo de usuario?
    @Query("SELECT DISTINCT tgu FROM Review rev JOIN rev.purchase p JOIN p.route rte JOIN rte.tourGuideList tgu WHERE rev.rating = :rating")
    List<TourGuideUser> findTourGuidesByRating(@Param("rating") int rating);

    @Query("FROM DriverUser du ORDER BY COUNT(du.routes) DESC")
    List<DriverUser> findDriversSortByRouteCountDesc(Pageable pageable);
}
