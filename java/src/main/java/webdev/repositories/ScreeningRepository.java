package webdev.repositories;

import org.springframework.data.repository.CrudRepository;

import webdev.models.Screening;

public interface ScreeningRepository
	extends CrudRepository<Screening, Integer> {
}