package webdev.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import webdev.models.Application;
import webdev.models.Movie;
import webdev.models.User;
import webdev.repositories.ApplicationRepository;
import webdev.repositories.MovieRepository;
import webdev.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials="true")
public class ApplicationServices {
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ApplicationRepository applicationRepository;

	@GetMapping("/api/applications")
	public Iterable<Application> findAllApplications() {
		return applicationRepository.findAll();
	}

	@GetMapping("/api/{movieId}/applications")
	public Iterable<Application> findAllApplicationsForMovieId(@PathVariable("movieId") int movieId) {
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			return movie.getApplications();
		}
		return null;
	}

	@GetMapping("/api/applications/user")
	public Iterable<Application> findAllApplicationsForUserId(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			return user.getApplications();
		}
		return null;
	}

	@DeleteMapping("/api/application/{applicationId}")
	public void deleteApplication(@PathVariable("applicationId") int applicationId) {
		Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
		if(optionalApplication.isPresent()) {
			applicationRepository.deleteById(applicationId);
		}
	}

	@DeleteMapping("/api/application")
	public void deleteAllApplicationsWithUserId(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		User user = optionalUser.get();
		List<Application> applications = user.getApplications();
		for(Application application : applications) {
			deleteApplication(application.getId());
		}
	}

	@DeleteMapping("/api/{movieId}/applications")
	public void deleteAllApplicationsWithMovieId(@PathVariable("movieId") int movieId) {
		Optional<Movie> optionalUser = movieRepository.findById(movieId);
		Movie movie = optionalUser.get();
		List<Application> applications = movie.getApplications();
		for(Application application : applications) {
			deleteApplication(application.getId());
		}
	}

	@GetMapping("/api/{movieId}/application")
	public ResponseEntity<String> getApplicationStatus(@PathVariable("movieId") int movieId, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<Integer> optionalApplicationId = applicationRepository.findIdByUserIdAndMovieId(currentUser, movieRepository.findById(movieId).get(),
				(String) session.getAttribute("loginType"));

		JSONObject bodyObject = new JSONObject("{}");
		HttpHeaders headers = new HttpHeaders();
		if (!optionalApplicationId.isPresent()) {
			bodyObject.put("status", "None");
			return new ResponseEntity<String>(bodyObject.toString(), headers, HttpStatus.ACCEPTED);
		}
		else {
			Optional<Application> optionalApplication = applicationRepository.findById(optionalApplicationId.get());
			Application application = optionalApplication.get();
			bodyObject.put("status", application.getStatus());
			return new ResponseEntity<String>(bodyObject.toString(), headers, HttpStatus.ACCEPTED);
		}
	}

	@PostMapping("/api/{movieId}/application")
	public Application addApplication(@RequestBody Application application, @PathVariable("movieId") int movieId,
																HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		application.setMovie(optionalMovie.get());
		application.setUser(optionalUser.get());
		application.setCreated(new Date(Calendar.getInstance().getTime().getTime()));
		application.setLoginType((String) session.getAttribute("loginType"));
		application.setStatus(Application.StatusEnums.pending);
		return applicationRepository.save(application);
	}

	@PutMapping("/api/{movieId}/application")
	public Application updateApplication(@RequestBody Application application, @PathVariable("movieId") int movieId,
																	 HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		application.setMovie(optionalMovie.get());
		application.setUser(optionalUser.get());
		return applicationRepository.save(application);
	}

	@PutMapping("/api/application/accepted/{applicationId}")
	public Application updateApplicationToAccepted(@PathVariable("applicationId") int applicationId) {
		Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
		if (optionalApplication.isPresent())
		{
			Application application = optionalApplication.get();
			application.setStatus(Application.StatusEnums.accepted);
			return applicationRepository.save(application);
		}
		return null;
	}
	
	@PutMapping("/api/application/rejected/{applicationId}")
	public Application updateApplicationToRejected(@PathVariable("applicationId") int applicationId) {
		Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
		if (optionalApplication.isPresent())
		{
			Application application = optionalApplication.get();
			application.setStatus(Application.StatusEnums.rejected);
			return applicationRepository.save(application);
		}
		return null;
	}

	@GetMapping("/api/{movieId}/applications/participants/accepted")
	public List<Application> getAcceptedParticipantApplicationsForMovies(@PathVariable("movieId") int movieId){
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			List<Application> applications = movie.getApplications();
			List<Application> newApplications = new ArrayList<Application>();
			for(Application application : applications) {
				if(application.getStatus().equals(Application.StatusEnums.accepted)) {
					if(application.getLoginType().equals("Participant"))
						newApplications.add(application);
				}
			}
			return newApplications;
		}
		return null;
	}

	@GetMapping("/api/{movieId}/applications/participants/pending")
	public List<Application> getPendingParticipantApplicationsForMovies(@PathVariable("movieId") int movieId){
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			List<Application> applications = movie.getApplications();
			List<Application> newApplications = new ArrayList<Application>();
			if (applications != null) {
				for(Application application : applications) {
					System.out.println(application.getLoginType());
					System.out.println(application.getStatus());
					if(application.getStatus().equals(Application.StatusEnums.pending)) {
						if(application.getLoginType().equals("Participant"))
							newApplications.add(application);
					}
				}
			}
			return newApplications;
		}
		return null;
	}

	@GetMapping("/api/{movieId}/applications/critics/accepted")
	public List<Application> getAcceptedCriticApplicationsForMovies(@PathVariable("movieId") int movieId){
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			List<Application> applications = movie.getApplications();
			List<Application> newApplications = new ArrayList<Application>();
			for(Application application : applications) {
				if(application.getStatus().equals(Application.StatusEnums.accepted)) {
					if(application.getLoginType().equals("Critic"))
						newApplications.add(application);
				}
			}
			return newApplications;
		}
		return null;
	}

	@GetMapping("/api/{movieId}/applications/critics/pending")
	public List<Application> getPendingCriticApplicationsForMovies(@PathVariable("movieId") int movieId){
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			List<Application> applications = movie.getApplications();
			List<Application> newApplications = new ArrayList<Application>();
			for(Application application : applications) {
				if(application.getStatus().equals(Application.StatusEnums.pending)) {
					if(application.getLoginType().equals("Critic"))
						newApplications.add(application);
				}
			}
			return newApplications;
		}
		return null;
	}
	
	@GetMapping("/api/applications/accepted/{userId}")
	public List<Movie> getAcceptedMoviesForOtherUser(@PathVariable("userId") int userId, HttpSession session){
		Optional<User> optionalUser = userRepository.findById(userId);
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<Application> applications = user.getApplications();
			List<Movie> movies = new ArrayList<Movie>();
			for(Application application : applications) {
				if(application.getStatus().equals(Application.StatusEnums.accepted)) {
					movies.add(application.getMovie());
				}
			}
			return movies;
		}
		return null;
	}

	@GetMapping("/api/applications/accepted")
	public List<Movie> getAcceptedMoviesForUser(HttpSession session){
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<Application> applications = user.getApplications();
			List<Movie> movies = new ArrayList<Movie>();
			for(Application application : applications) {
				if(application.getStatus().equals(Application.StatusEnums.accepted)
						&& (application.getLoginType().equals((String) session.getAttribute("loginType")))) {
					movies.add(application.getMovie());
				}
			}
			return movies;
		}
		return null;
	}

	@GetMapping("/api/applications/rejected")
	public List<Movie> getRejectedMoviesForUser(HttpSession session){
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<Application> applications = user.getApplications();
			List<Movie> movies = new ArrayList<Movie>();
			for(Application application : applications) {
				if(application.getStatus().equals(Application.StatusEnums.rejected)
						&& (application.getLoginType().equals((String) session.getAttribute("loginType")))) {
					movies.add(application.getMovie());
				}
			}
			return movies;
		}
		return null;
	}

	@GetMapping("/api/applications/pending")
	public List<Movie> getPendingMoviesForUser(HttpSession session){
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<Application> applications = user.getApplications();
			List<Movie> movies = new ArrayList<Movie>();
			for(Application application : applications) {
				if(application.getStatus().equals(Application.StatusEnums.pending)
						&& (application.getLoginType().equals((String) session.getAttribute("loginType")))) {
					System.out.println(application.getLoginType());
					System.out.println((String) session.getAttribute("loginType"));
					movies.add(application.getMovie());
				}
			}
			return movies;
		}
		return null;
	}
}
