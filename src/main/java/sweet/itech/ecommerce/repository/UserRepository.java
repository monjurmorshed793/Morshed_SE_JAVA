package sweet.itech.ecommerce.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import sweet.itech.ecommerce.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByEmailIgnoreCaseAndPassword(String email, String password);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);
}
