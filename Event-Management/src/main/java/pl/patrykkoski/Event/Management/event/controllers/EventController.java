package pl.patrykkoski.Event.Management.event.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.patrykkoski.Event.Management.event.entities.Event;
import pl.patrykkoski.Event.Management.event.repositories.EventRepository;

import javax.validation.Valid;

@RestController
public class EventController {

    private int eventCapacity = 120;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/events")
    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @PostMapping("/events")
    public Event createEvent(@Valid @RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PutMapping("/events/{eventId}")
    public Event updateEvent(@PathVariable Long eventId, @Valid @RequestBody Event eventRequest) {
        return eventRepository.findById(eventId).map(event -> {
            event.setEventName(eventRequest.getEventName());
            event.setEventDescription(eventRequest.getEventDescription());
            event.setEventDate(eventRequest.getEventDate());
            event.setEventPlace(eventRequest.getEventPlace());
            event.setEventCapacity(this.eventCapacity);
            return eventRepository.save(event);
        }).orElseThrow(() -> new ResourceNotFoundException("EventID " + eventId + " not found"));
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) {
        return eventRepository.findById(eventId).map(event -> {
            eventRepository.delete(event);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("EventId " + eventId + " not found"));
    }
}
