package mn.easybuy.customerservice.repository;

import mn.easybuy.customerservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findById(Integer id);
}
