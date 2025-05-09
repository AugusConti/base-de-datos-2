package unlp.info.bd2.repositories;
 

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

    long countByRouteAndDate(Route route, Date date);

    Optional<Purchase> findByCode(String code);

    List<Purchase> findAllByUserUsername(String username);

    Long countByDateBetween(@Param("start") Date start, @Param("end") Date end);

    List<Purchase> findAllByItemServiceListService(@Param("service") Service service);

    //@Query("SELECT DISTINCT p FROM Purchase p JOIN p.itemServiceList iserv ORDER BY p.totalPrice DESC")
    //List<Purchase> findTop10MoreExpensivePurchasesWithServices(Pageable pageable);
    List<Purchase> findByItemServiceListIsNotEmptyOrderByTotalPriceDesc(Pageable pageable);
}
