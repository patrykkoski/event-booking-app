package pl.patrykkoski.Event.Management.ticket.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.patrykkoski.Event.Management.ticket.entities.Ticket;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByEventId(Long eventId, Pageable pageable);
    Optional<Ticket> findByIdAndEventId(Long id, Long eventId);
}
