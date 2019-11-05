package pl.patrykkoski.Event.Management.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.patrykkoski.Event.Management.user.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
