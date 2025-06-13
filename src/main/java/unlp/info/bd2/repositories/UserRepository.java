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

    @Aggregation(pipeline = {"""
        {$match: {
            _class: 'unlp.info.bd2.model.User',
            $expr: {$gte: [{$size: '$purchaseList'}, ?0]}
        }}"""
    })
    List<User> findByPurchaseCountGreaterThan(int number);

    @Aggregation(pipeline = {
        "{$addFields: {purchaseCount: {$size: '$purchaseList'}}}",
        "{$sort: {purchaseCount: -1}}",
        "{$unset: 'purchaseCount'}"
    })
    List<User> findAllSortByPurchaseCountDesc(Pageable pageable);


    //Esta query sabemos que se puede hacer solo con el nombre del método, pero no nos andaba, así que la hice con un solo @aggregation y anduvo. Ya no hacen falta dos queries
    @Aggregation(pipeline = {
            "{ $match: { '_class': 'unlp.info.bd2.model.User' } }",
            "{ $lookup: { from: 'purchase', localField: '_id', foreignField: 'user.$id', as: 'purchases' } }",
            "{ $match: { 'purchases.totalPrice': { $gte: ?0 } } }",
            "{ $project: { purchases: 0 } }"
    })
    List<User> findUsersWithSinglePurchaseMoreThan(float mount);
}