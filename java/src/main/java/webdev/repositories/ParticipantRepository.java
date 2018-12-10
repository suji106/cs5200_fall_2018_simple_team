package webdev.repositories;

import org.springframework.data.repository.CrudRepository;

import webdev.models.Participant;

public interface ParticipantRepository
	extends CrudRepository<Participant, Integer> {
	
}