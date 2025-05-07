package unlp.info.bd2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import unlp.info.bd2.model.Supplier;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByAuthorizationNumber(String authorizationNumber);
}
