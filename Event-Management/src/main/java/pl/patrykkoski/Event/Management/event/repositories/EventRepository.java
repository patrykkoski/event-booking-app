package pl.patrykkoski.Event.Management.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.patrykkoski.Event.Management.event.entities.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
