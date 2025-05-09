package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByPurchaseListTotalPriceGreaterThanEqual(float mount);

    @Query("SELECT u FROM User u WHERE SIZE(u.purchaseList) >= :number")
    List<User> findByPurchaseCountGreaterThan(@Param("number") int number);

    @Query("SELECT u FROM User u ORDER BY SIZE(u.purchaseList) DESC")
    List<User> findAllSortByPurchaseCountDesc(Pageable pageable);
}
