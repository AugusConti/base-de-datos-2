package unlp.info.bd2.repositories;

import org.springframework.data.repository.CrudRepository;
import unlp.info.bd2.model.Supplier;

import java.util.Optional;

public interface SupplierRepository extends CrudRepository<Supplier, Long> {
    Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);
}
