package unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import unlp.info.bd2.model.TourGuideUser;

public interface TourGuideUserRepository extends BaseUserRepository<TourGuideUser> {

    @Query("SELECT DISTINCT tgu FROM Review rev JOIN rev.purchase p JOIN p.route rte JOIN rte.tourGuideList tgu WHERE rev.rating = :rating")
    List<TourGuideUser> findByRating(@Param("rating") int rating);

}
