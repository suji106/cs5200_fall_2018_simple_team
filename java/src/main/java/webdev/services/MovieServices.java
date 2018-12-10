package webdev.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import webdev.models.Movie;
import webdev.models.User;
import webdev.repositories.MovieRepository;
import webdev.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials="true")
public class MovieServices {
	@Autowired
  MovieRepository movieRepository;

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/api/movies")
	public Iterable<Movie> getAllMovies() {
		return movieRepository.findAll();
	}

	@GetMapping("/api/movie/{movieId}")
	public Movie getMovieById(@PathVariable("movieId") int movieId) {
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			return optionalMovie.get();
		}
		return null;
	}

	@GetMapping("/api/movies/host")
	public List<Movie> getMoviesByCurrentHostId(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			return user.getMoviesPosted();
		}
		return null;
	}
	
	@GetMapping("/api/movies/host/{userId}")
	public List<Movie> getMoviesByHostId(@PathVariable("userId") int userId, HttpSession session) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			return user.getMoviesPosted();
		}
		return null;
	}
	
	@GetMapping("/api/movie/{movieId}/host")
	public ResponseEntity<String> movieOwnedByHost(@PathVariable("movieId") int movieId, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		JSONObject bodyObject = new JSONObject("{}");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			if (currentUser.getId() == movie.getHost().getId()) {
				bodyObject.put("isHost", "true");
				return new ResponseEntity<String>(bodyObject.toString(), headers, HttpStatus.ACCEPTED);
			}
		}
		bodyObject.put("isHost", "false");
		return new ResponseEntity<String>(bodyObject.toString(), headers, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/api/movie/{movieId}")
	public void deleteMovie(@PathVariable("movieId") int movieId) {
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			movieRepository.deleteById(movieId);
		}
	}

	@PostMapping("/api/movie")
	public Movie addMovie(@RequestBody Movie movie, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		if(optionalUser.isPresent()) {
			movie.setHost(optionalUser.get());
			return movieRepository.save(movie);
		}
		return null;
	}
	
	@PutMapping("/api/movie")
	public Movie updateMovie(@RequestBody Movie movie, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		if(optionalUser.isPresent()) {
			movie.setHost(optionalUser.get());
			return movieRepository.save(movie);
		}
		return null;
	}

	@PostMapping("/api/specialized")
	public List<Movie> getSpecializedMovies(@RequestBody String params) {
		params = params.replaceAll("\"", "");
		List<Movie> movies = (List<Movie>) getAllMovies();
		if (params.trim().length() > 0) {
			List<Movie> specialMovies = new ArrayList<Movie>();
			String[] reqParams = params.split(" ", params.length());
			for(Movie movie: movies) {
				for(String param: reqParams)  {

					String genreList = movie.getGenre();
						if(genreList.toLowerCase().contains(param.toLowerCase())) {
							specialMovies.add(movie);
					}

					String castList = movie.getCast();
					if(castList.toLowerCase().contains(param.toLowerCase())) {
						specialMovies.add(movie);
					}

					String langList = movie.getLanguages();
					if(langList.toLowerCase().contains(param.toLowerCase())) {
						specialMovies.add(movie);
					}

					String title = movie.getTitle();
					if(title.toLowerCase().contains(param.toLowerCase())) {
						specialMovies.add(movie);
					}


				}
			}
		return specialMovies.stream().distinct().collect(Collectors.toList());
		}
		else
			return movies;
	}
}
