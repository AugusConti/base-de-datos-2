package unlp.info.bd2.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Supplier;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, ObjectId> {
    Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);

    @Query("SELECT s FROM Supplier s JOIN s.services ser JOIN ser.itemServiceList iserv JOIN iserv.purchase p " +
            "GROUP BY s " +
            "ORDER BY SUM(p.totalPrice) DESC")
    List<Supplier> findTopNSuppliersInPurchases(Pageable pageable);


    @Query("SELECT s FROM Supplier s JOIN s.services ser JOIN ser.itemServiceList iser "+
            "GROUP BY s.id " +
            "ORDER BY SUM(iser.quantity) DESC")
    List<Supplier> findTopNSuppliersItemsSold(Pageable pageable);

    @Query("SELECT MAX(SIZE(s.services)) FROM Supplier s")
    Long countMaxServices();

}