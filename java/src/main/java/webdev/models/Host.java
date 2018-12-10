package webdev.models;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Host extends User{
	@OneToMany(mappedBy="host")
	@JsonIgnore
	private List<Movie> moviesPosted;

	public List<Movie> getMoviesPosted() {
		return moviesPosted;
	}

	public void setMoviesPosted(List<Movie> moviesPosted) {
		this.moviesPosted = moviesPosted;
	}


}