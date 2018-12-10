package webdev.repositories;

import org.springframework.data.repository.CrudRepository;

import webdev.models.Critic;

public interface CriticRepository
	extends CrudRepository<Critic, Integer> {
	
}