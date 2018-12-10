package webdev.repositories;

import org.springframework.data.repository.CrudRepository;

import webdev.models.Host;

public interface HostRepository
	extends CrudRepository<Host, Integer> {
	
}