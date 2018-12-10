package webdev.models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private String email;
	private String password;
	private Date created;
	private String rottenUrl;
	private String imdbUrl ;
	@JsonIgnore
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = CascadeType.PERSIST)
	private List<Review> reviews;
	@JsonIgnore
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = CascadeType.PERSIST)
	private List<Application> applications;
	@JsonIgnore
	@OneToMany(mappedBy="critic", orphanRemoval = true, cascade = CascadeType.PERSIST)
	private List<Screening> screenings;
	@OneToMany(mappedBy="host", orphanRemoval = true, cascade = CascadeType.PERSIST)
	@JsonIgnore
	private List<Movie> moviesPosted;
	@JsonIgnore
	@ManyToMany(mappedBy="usersFollowed")
	private List<User> usersFollowing;
	@JsonIgnore
	@ManyToMany
	private List<User> usersFollowed;

	public List<Movie> getMoviesPosted() {
		return moviesPosted;
	}

	public int getId() {
		return id;
	}
	public List<Application> getApplications() {
		return applications;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getRottenUrl() {
		return rottenUrl;
	}

	public void setRottenUrl(String rottenUrl) {
		this.rottenUrl = rottenUrl;
	}

	public String getImdbUrl() {
		return imdbUrl;
	}

	public void setImdbUrl(String imdbUrl) {
		this.imdbUrl = imdbUrl;
	}
	public String getPassword() {
		return this.password;
	}
	public List<Screening> getScreenings() {
		return screenings;
	}

	public List<User> getUsersFollowing() {
		return usersFollowing;
	}
	public List<User> getUsersFollowed() {
		return usersFollowed;
	}
	public void setUsersFollowed(List<User> usersFollowed) {
		this.usersFollowed = usersFollowed;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreated() {
		return created;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public void setScreenings(List<Screening> screenings) {
		this.screenings = screenings;
	}

	public void setMoviesPosted(List<Movie> moviesPosted) {
		this.moviesPosted = moviesPosted;
	}

	public void setUsersFollowing(List<User> usersFollowing) {
		this.usersFollowing = usersFollowing;
	}
}