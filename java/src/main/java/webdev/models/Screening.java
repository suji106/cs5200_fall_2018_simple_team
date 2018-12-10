package webdev.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Screening {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String title;
	private String details;
	private String venue;
	private Date screeningTime;
	@ManyToOne
	private User critic;
	@ManyToOne
	private Movie movie;
	public int getId() {
		return id;
	}

	public void setCritic(User critic) {
		this.critic = critic;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public Date getScreeningTime() {
		return screeningTime;
	}

	public void setScreeningTime(Date screeningTime) {
		this.screeningTime = screeningTime;
	}

	public User getCritic() {
		return critic;
	}
}
