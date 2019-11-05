package pl.patrykkoski.Event.Management.ticket.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import pl.patrykkoski.Event.Management.event.entities.Event;
import pl.patrykkoski.Event.Management.ticket.entities.Ticket;
import pl.patrykkoski.Event.Management.user.entities.User;
import pl.patrykkoski.Event.Management.event.repositories.EventRepository;
import pl.patrykkoski.Event.Management.ticket.repositories.TicketRepository;
import pl.patrykkoski.Event.Management.user.repositories.UserRepository;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Function returns all tickets for given event with pagination.
     *
     * @param eventId
     * @param pageable
     * @return
     */
    @GetMapping("/events/{eventId}/tickets")
    public Page<Ticket> getAllTicketsByEventId(@PathVariable(value = "eventId") Long eventId,
                                               Pageable pageable) {

        return ticketRepository.findByEventId(eventId, pageable);
    }

    /**
     * Function that adds new ticket for given event.
     *
     * @param eventId
     * @param ticket
     * @return
     */
    @PostMapping("/events/{eventId}/addTicket")
    public Ticket createTicket(@PathVariable(value = "eventId") Long eventId,
                               @Valid @RequestBody Ticket ticket) {

        return eventRepository.findById(eventId).map(event -> {
            ticket.setEvent(event);
            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new ResourceNotFoundException("EventId " + eventId + " not found"));
    }

    /**
     * Function that adds new tickets for given event. Tickets count is determined by event capacity.
     *
     * @param eventId
     */
    @PostMapping("events/{eventId}/addTickets")
    public void addTicketsForEvent(@PathVariable(value = "eventId") Long eventId,
                                   @PathVariable(value = "price") double price) {

        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Event event = eventOptional.get();
        int capacity = event.getEventCapacity();

        for (int i = 1; i <= capacity; i++) {
            Ticket ticket = new Ticket();
            ticket.setUser(userRepository.findById(1L).get());
            ticket.setSeatNumber(i);
            ticket.setEvent(event);
            ticket.setPrice(price);

            ticketRepository.save(ticket);
        }
    }

    /**
     *  Function that supports the purchase of ticket for a given user.
     *
     * @param eventId
     * @param userId
     * @param ticketId
     */
    @GetMapping("events/{eventId}/buyTicket/{userId}/{ticketId}")
    public void addTicketForUser(@PathVariable(value = "eventId") Long eventId,
                                 @PathVariable(value = "userId") Long userId,
                                 @PathVariable(value = "ticketId") Long ticketId) {

        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();
        Optional<Ticket> ticketOptional = ticketRepository.findByIdAndEventId(ticketId, eventId);
        Ticket ticket = ticketOptional.get();

        ticket.setUser(user);
        ticketRepository.save(ticket);
    }


}
