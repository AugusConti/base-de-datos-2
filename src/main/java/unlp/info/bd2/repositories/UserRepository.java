package unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.User;

@Repository
public interface UserRepository extends BaseUserRepository<User> {
    @Query("SELECT DISTINCT u FROM User u JOIN u.purchaseList p WHERE p.totalPrice >= :mount")
    List<User> findByMountSpendingGreaterThan(@Param("mount") float mount);

    // TODO CONUSLTAR Formato de queries (de otra forma tira error)
    @Query("SELECT u FROM User u JOIN u.purchaseList p GROUP BY u.id HAVING COUNT(p) >= :number")
    List<User> findByPurchaseCountGreaterThan(@Param("number") int number);

    @Query("SELECT u FROM User u JOIN u.purchaseList p GROUP BY u.id ORDER BY COUNT(p) DESC")
    List<User> findAllSortByPurchaseCountDesc(Pageable pageable);
}
