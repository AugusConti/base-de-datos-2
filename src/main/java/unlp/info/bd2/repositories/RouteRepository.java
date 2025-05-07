package unlp.info.bd2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import unlp.info.bd2.model.Route;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByPriceLessThan(float price);
}
