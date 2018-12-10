package webdev.models;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Application {
	public enum StatusEnums {
		accepted, rejected, pending
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String details;
	private Date created;
	private String loginType;

	public void setId(int id) {
		this.id = id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getCreated() {
		return created;
	}

	@Enumerated
	private StatusEnums status;
	@ManyToOne
	private User user;
	@ManyToOne
	private Movie movie;
	

	public void setCreated(Date created) {
		this.created = created;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	public int getId() {
		return id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public StatusEnums getStatus() {
		return status;
	}
	public void setStatus(StatusEnums status) {
		this.status = status;
	}
}
