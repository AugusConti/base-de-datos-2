package unlp.info.bd2.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import unlp.info.bd2.model.User;

@NoRepositoryBean
public interface BaseUserRepository<T extends User> extends CrudRepository<T, Long> {
    Optional<T> findByUsername(String username);

    boolean existsByUsername(String username);
}
