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

import webdev.models.Critic;
import webdev.repositories.CriticRepository;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials="true")
public class CriticServices {
	@Autowired
	CriticRepository criticRepository;
	
	@GetMapping("/api/critics")
	public Iterable<Critic> findAllCritics() {
		return criticRepository.findAll();
	}
	
	@DeleteMapping("/api/critic/{criticId}")
	public void deleteCritic(@PathVariable("criticId") int criticId) {
		Optional<Critic> optionalCritic = criticRepository.findById(criticId);
		if(optionalCritic.isPresent()) {
			criticRepository.deleteById(criticId);
		}
	}
	
	@PostMapping("/api/critic")
	public Critic addCritic(@RequestBody Critic critic) {
		Optional<Critic> optionalCritic = criticRepository.findById(critic.getId());
		if(!optionalCritic.isPresent()) {
			return criticRepository.save(critic);
		}
		return null;
	}
	
	@PutMapping("/api/critic")
	public Critic updateCritic(@RequestBody Critic critic) {
		Optional<Critic> optionalCritic = criticRepository.findById(critic.getId());
		if(optionalCritic.isPresent()) {
			return criticRepository.save(critic);
		}
		return null;
	}
}