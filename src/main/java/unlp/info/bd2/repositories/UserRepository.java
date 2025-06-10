package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByPurchaseListTotalPriceGreaterThanEqual(float mount);

    @Query("SELECT u FROM User u WHERE SIZE(u.purchaseList) >= :number")
    List<User> findByPurchaseCountGreaterThan(@Param("number") int number);

    @Aggregation(pipeline = {
        "{$addFields: {purchaseCount: {$size: '$purchaseList'}}}",
        "{$sort: {purchaseCount: -1}}",
        "{$unset: 'purchaseCount'}"
    })
    List<User> findAllSortByPurchaseCountDesc(Pageable pageable);
}