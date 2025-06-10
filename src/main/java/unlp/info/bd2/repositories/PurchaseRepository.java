package unlp.info.bd2.repositories;
 

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, ObjectId> {

    long countByRouteAndDate(Route route, Date date);

    Optional<Purchase> findByCode(String code);

    List<Purchase> findAllByUserId(ObjectId userId);

    Long countByDateBetween(@Param("start") Date start, @Param("end") Date end);

    List<Purchase> findAllByItemServiceListService(@Param("service") Service service);

    //@Query("SELECT DISTINCT p FROM Purchase p JOIN p.itemServiceList iserv ORDER BY p.totalPrice DESC")
    //List<Purchase> findTop10MoreExpensivePurchasesWithServices(Pageable pageable);
    List<Purchase> findByItemServiceListIsNotEmptyOrderByTotalPriceDesc(Pageable pageable);
}