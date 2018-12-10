package webdev.services;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import webdev.models.Screening;
import webdev.models.Movie;
import webdev.models.User;
import webdev.repositories.ScreeningRepository;
import webdev.repositories.MovieRepository;
import webdev.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials="true")
public class ScreeningServices {
	@Autowired
	ScreeningRepository screeningRepository;
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/api/screenings")
	public Iterable<Screening> findAllScreenings() {
		return screeningRepository.findAll();
	}
	
	@DeleteMapping("/api/screening/{screeningId}")
	public void deleteScreening(@PathVariable("screeningId") int screeningId) {
		Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);
		if(optionalScreening.isPresent()) {
			screeningRepository.deleteById(screeningId);
		}
	}
	
	@PostMapping("/api/{movieId}/screening")
	public Screening addScreening(@RequestBody Screening screening, @PathVariable("movieId") int movieId,
														 HttpSession session) {
		User currentCritic = (User) session.getAttribute("currentUser");
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		Optional<User> optionalUser = userRepository.findById(currentCritic.getId());
		screening.setMovie(optionalMovie.get());
		screening.setCritic(optionalUser.get());
		return screeningRepository.save(screening);
	}
	
	@PutMapping("/api/{movieId}/screening")
	public Screening updateApplication(@RequestBody Screening screening, @PathVariable("movieId") int movieId,
																 HttpSession session) {
		User currentCritic = (User) session.getAttribute("currentUser");
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		Optional<User> optionalUser = userRepository.findById(currentCritic.getId());
		screening.setMovie(optionalMovie.get());
		screening.setCritic(optionalUser.get());
		return screeningRepository.save(screening);
	}
	
	@GetMapping("/api/{movieId}/screenings")
	public List<Screening> getScreeningsForMovies(@PathVariable("movieId") int movieId){
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			Movie Movie = optionalMovie.get();
			List<Screening> screenings = Movie.getScreenings();
			return screenings;
		}
		return null;
	}
	
	@GetMapping("/api/user/screenings")
	public List<Screening> getScreeningsForCritic(HttpSession session) {
		User currentCritic = (User) session.getAttribute("currentUser");
		Optional<User> optionalCritic = userRepository.findById(currentCritic.getId());
		if(optionalCritic.isPresent()) {
			List<Screening> screenings = currentCritic.getScreenings();
			return screenings;
		}
		return null;
	}
}