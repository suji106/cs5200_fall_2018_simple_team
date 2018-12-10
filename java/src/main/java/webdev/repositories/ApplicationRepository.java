package webdev.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import webdev.models.Movie;
import webdev.models.Application;
import webdev.models.User;

public interface ApplicationRepository
	extends CrudRepository<Application, Integer> {
	@Query("SELECT id FROM Application r WHERE r.user=:user and r.movie=:movie and r.loginType = :loginType")
	Optional<Integer> findIdByUserIdAndMovieId(
		@Param("user") User user, @Param("movie") Movie movie, @Param("loginType") String loginType);
}