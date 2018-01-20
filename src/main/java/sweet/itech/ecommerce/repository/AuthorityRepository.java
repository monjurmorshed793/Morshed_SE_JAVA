package sweet.itech.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sweet.itech.ecommerce.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
