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

import webdev.models.Host;
import webdev.repositories.HostRepository;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials="true")
public class HostServices {
	@Autowired
	HostRepository hostRepository;
	
	@GetMapping("/api/hosts")
	public Iterable<Host> findAllHosts() {
		return hostRepository.findAll();
	}
	
	@DeleteMapping("/api/host/{hostId}")
	public void deleteHost(@PathVariable("hostId") int hostId) {
		Optional<Host> optionalHost = hostRepository.findById(hostId);
		if(optionalHost.isPresent()) {
			hostRepository.deleteById(hostId);
		}
	}
	
	@PostMapping("/api/host")
	public Host addHost(@RequestBody Host host) {
		Optional<Host> optionalHost = hostRepository.findById(host.getId());
		if(!optionalHost.isPresent()) {
			return hostRepository.save(host);
		}
		return null;
	}
	
	@PutMapping("/api/host")
	public Host updateHost(@RequestBody Host host) {
		Optional<Host> optionalHost = hostRepository.findById(host.getId());
		if(optionalHost.isPresent()) {
			return hostRepository.save(host);
		}
		return null;
	}
}