package webdev.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import webdev.models.Participant;
import webdev.repositories.ParticipantRepository;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials="true")
public class ParticipantServices {
	@Autowired
	ParticipantRepository participantRepository;
	
	@GetMapping("/api/participants")
	public Iterable<Participant> findAllParticipants() {
		return participantRepository.findAll();
	}
	
	@DeleteMapping("/api/participant/{participantId}")
	public void deleteParticipant(@PathVariable("participantId") int participantId) {
		Optional<Participant> optionalParticipant = participantRepository.findById(participantId);
		if(optionalParticipant.isPresent()) {
			participantRepository.deleteById(participantId);
		}
	}
	
	@PostMapping("/api/participant")
	public Participant addParticipant(@RequestBody Participant participant) {
		Optional<Participant> optionalParticipant = participantRepository.findById(participant.getId());
		if(!optionalParticipant.isPresent()) {
			return participantRepository.save(participant);
		}
		return null;
	}
	
	@PutMapping("/api/participant")
	public Participant updateParticipant(@RequestBody Participant participant) {
		Optional<Participant> optionalParticipant = participantRepository.findById(participant.getId());
		if(optionalParticipant.isPresent()) {
			return participantRepository.save(participant);
		}
		return null;
	}
}