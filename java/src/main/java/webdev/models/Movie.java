package webdev.models;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Movie {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String title;
	private String summary;
	private String cast;
	private String genre;
	private String languages;
	private String poster;



	public String getLanguages() {
		return languages;
	}


	public String getGenre() {
		return genre;
	}



	@JsonIgnore
	@ManyToOne
	private User host;
	@OneToMany(mappedBy="movie",  orphanRemoval = true, cascade = CascadeType.PERSIST)
	@JsonIgnore
	private List<Review> reviews;
	@OneToMany(mappedBy="movie", orphanRemoval = true, cascade = CascadeType.PERSIST)
	@JsonIgnore
	private List<Screening> screenings;
	@OneToMany(mappedBy="movie", orphanRemoval = true, cascade = CascadeType.PERSIST)
	@JsonIgnore
	private List<Application> applications;
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}

	public User getHost() {
		return host;
	}
	public void setHost(User host) {
		this.host = host;
	}
	public String getCast() {
		return cast;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public List<Screening> getScreenings() {
		return screenings;
	}
	public List<Application> getApplications() {
		return applications;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public void setScreenings(List<Screening> screenings) {
		this.screenings = screenings;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
}