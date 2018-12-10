package webdev.repositories;

import org.springframework.data.repository.CrudRepository;

import webdev.models.Review;

public interface ReviewRepository
	extends CrudRepository<Review, Integer> {
	
}