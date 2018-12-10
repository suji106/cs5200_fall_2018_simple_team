package webdev.repositories;

import org.springframework.data.repository.CrudRepository;

import webdev.models.Movie;

public interface MovieRepository
	extends CrudRepository<Movie, Integer> {
	
}