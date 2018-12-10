package webdev.services;

import java.sql.Date;
import java.util.Calendar;
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

import webdev.models.Review;
import webdev.models.Movie;
import webdev.models.User;
import webdev.repositories.ReviewRepository;
import webdev.repositories.MovieRepository;
import webdev.repositories.UserRepository;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials="true")
public class ReviewServices {
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ReviewRepository reviewRepository;
	
	@GetMapping("/api/reviews")
	public Iterable<Review> findAllReviews() {
		return reviewRepository.findAll();
	}
	
	@GetMapping("/api/{movieId}/movie/reviews")
	public Iterable<Review> findAllReviewsForMovieId(@PathVariable("movieId") int movieId) {
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		System.out.println(movieId);
		if(optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			List<Review> reviews = movie.getReviews();
			System.out.println(reviews.size());
			reviews.sort((o1, o2) -> o1.getCreated().compareTo(o2.getCreated()));
			return reviews;
		}
		return null;
	}
	
	@GetMapping("/api/user/reviews")
	public Iterable<Review> findAllReviewsForUserId(@PathVariable("userId") int userId, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<Review> reviews = user.getReviews();
			reviews.sort((o1, o2) -> o1.getCreated().compareTo(o2.getCreated()));
			return reviews;
		}
		return null;
	}
	
	@DeleteMapping("/api/review/{reviewId}")
	public void deleteReview(@PathVariable("reviewId") int reviewId) {
		Optional<Review> optionalReview = reviewRepository.findById(reviewId);
		if(optionalReview.isPresent()) {
			reviewRepository.deleteById(reviewId);
		}
	}
	
	@DeleteMapping("/api/{movieId}/reviews")
	public void deleteAllReviewsWithMovieId(@PathVariable("movieId") int movieId) {
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		Movie movie = optionalMovie.get();
		List<Review> reviews = movie.getReviews();
		for(Review review : reviews) {
			deleteReview(review.getId());
		}
	}
	
	@PutMapping("/api/{movieId}/review")
	public Review updateReview(@RequestBody Review review, HttpSession session,
                              @PathVariable("movieId") int movieId) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent() && optionalUser.isPresent()) {
			review.setMovie(optionalMovie.get());
			review.setUser(optionalUser.get());
			return reviewRepository.save(review);
		}
		return null;
	}
	
	@PostMapping("/api/{movieId}/review")
	public Review addReview(@RequestBody Review review, @PathVariable("movieId") int movieId,
                           HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<User> optionalUser = userRepository.findById(currentUser.getId());
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent() && optionalUser.isPresent()) {
			review.setCreated(new Date(Calendar.getInstance().getTime().getTime()));
			review.setMovie(optionalMovie.get());
			review.setUserType((String) session.getAttribute("loginType"));
			review.setUser(optionalUser.get());
			return reviewRepository.save(review);
		}
		return null;
	}
}