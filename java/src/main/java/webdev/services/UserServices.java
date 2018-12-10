package webdev.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.JSONException;
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

import webdev.models.User;
import webdev.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials="true")
public class UserServices {
	@Autowired
	UserRepository ur;

	@GetMapping("/api/users")
	public Iterable<User> findAllUsers() {
		return ur.findAll(); 
	}

	@GetMapping("/api/user/{id}")
	public User getUser(@PathVariable("id") int id, HttpSession currentSession) {
		Optional<User> temp = ur.findById(id);
		if(temp.isPresent()) {
			return temp.get();
		}
		return null;
	}

	@GetMapping("/api/user")
	public User getUser(HttpSession currentSession) {
		User currentUser = (User) currentSession.getAttribute("currentUser");
		Optional<User> temp = ur.findById(currentUser.getId());
		if(temp.isPresent()) {
			return temp.get();
		}
		return null;
	}

	@DeleteMapping("/api/user/{id}")
	public void deleteUserWithUserId(@PathVariable("id") int id) {
		Optional<User> temp = ur.findById(id);
		if(temp.isPresent()) {
			ur.deleteById(temp.get().getId());
		}
	}

	@DeleteMapping("/api/user")
	public void deleteUser(HttpSession currentSession) {
		User currentUser = (User) currentSession.getAttribute("currentUser");
		Optional<User> temp = ur.findById(currentUser.getId());
		if(temp.isPresent()) {
			ur.deleteById(currentUser.getId());
		}
	}

	@GetMapping("/api/profile/{email}")
	public User findUserByUsername(@PathVariable("email") String email) {
		Optional<User> data = ur.findUserByUsername(email);
		if(data.isPresent()) {
			return data.get();
		}
		return null;
	}

	@PostMapping("/api/user/signup")
	public User signUpUser(@RequestBody User user, HttpSession currentSession) {
		Optional<User> temp = ur.findById(user.getId());
		if(!temp.isPresent()) {
			user.setCreated(new Date(Calendar.getInstance().getTime().getTime()));
			System.out.println(System.currentTimeMillis());
			return ur.save(user);
		}
		return null;
	}

	@GetMapping("/api/user/follow/{id}")
	public User followUser(@PathVariable("id") int id, HttpSession currentSession) {
		User currentUser = (User) currentSession.getAttribute("currentUser");
		Optional<User> temp = ur.findById(currentUser.getId());
		if (temp.isPresent()) {
			User user = temp.get();
			List<User> usersFollowed = user.getUsersFollowed();
			usersFollowed = new ArrayList<User>(usersFollowed);
			Optional<User> tempToBeFollowed = ur.findById(id);
			if (tempToBeFollowed.isPresent()) {
				User userToBeFollowed = tempToBeFollowed.get();
				usersFollowed.add(userToBeFollowed);
				user.setUsersFollowed(usersFollowed);
				return ur.save(user);
			}
		}
		return null;
	}
	
	@GetMapping("/api/user/follows")
	public List<User> follows(HttpSession currentSession) {
		User currentUser = (User) currentSession.getAttribute("currentUser");
		Optional<User> temp = ur.findById(currentUser.getId());
		if (temp.isPresent()) {
			User user = temp.get();
			return user.getUsersFollowed();
		}
		return null;
	}
	
	@GetMapping("/api/user/followers")
	public List<User> followers(HttpSession currentSession) {
		User currentUser = (User) currentSession.getAttribute("currentUser");
		Optional<User> temp = ur.findById(currentUser.getId());
		if (temp.isPresent()) {
			User user = temp.get();
			return user.getUsersFollowing();
		}
		return null;
	}
	
	@GetMapping("/api/user/follows/{id}")
	public ResponseEntity<String> followsUser(@PathVariable("id") int id, HttpSession currentSession) {
		User currentUser = (User) currentSession.getAttribute("currentUser");
		Optional<User> temp = ur.findById(currentUser.getId());
		JSONObject jsonBody = new JSONObject("{}");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (temp.isPresent()) {
			User user = temp.get();
			List<User> usersFollowed = user.getUsersFollowed();
			usersFollowed = new ArrayList<User>(usersFollowed);
			Optional<User> tempToBeFollowed = ur.findById(id);
			if (tempToBeFollowed.isPresent()) {
				User userToBeFollowed = tempToBeFollowed.get();
				for (User checkUser: usersFollowed) {
					if (checkUser.getEmail().equals(userToBeFollowed.getEmail())) {
						jsonBody.put("follows", "true");
						return new ResponseEntity<String>(jsonBody.toString(), headers, HttpStatus.ACCEPTED); 
					}
				}
			}
		}
		jsonBody.put("follows", "false");
		return new ResponseEntity<String>(jsonBody.toString(), headers, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/api/user/unfollow/{id}")
	public User unfollowUser(@PathVariable("id") int id, HttpSession currentSession) {
		User currentUser = (User) currentSession.getAttribute("currentUser");
		Optional<User> temp = ur.findById(currentUser.getId());
		if (temp.isPresent()) {
			User user = temp.get();
			List<User> usersFollowed = user.getUsersFollowed();
			usersFollowed = new ArrayList<User>(usersFollowed);
			Optional<User> tempToBeFollowed = ur.findById(id);
			if (tempToBeFollowed.isPresent()) {
				User userToBeFollowed = tempToBeFollowed.get();
				usersFollowed.remove(userToBeFollowed);
				user.setUsersFollowed(usersFollowed);
				return ur.save(user);
			}
		}
		return null;
	}

	@PostMapping("/api/user/normalLogin")
	public User normalLoginUser(@RequestBody String body, HttpSession currentSession) throws JSONException {
		JSONObject jsonBody = new JSONObject(body);
		String loginType = (String) jsonBody.get("loginType");
		currentSession.setAttribute("loginType", loginType);
		String email = (String) jsonBody.get("email");
		String password = (String) jsonBody.get("password");
		Optional<Integer> optionalId = ur.findUserIdByEmail(email);
		if(optionalId.isPresent()) {
			Optional<User> temp = ur.findById(optionalId.get());
			if (temp.get().getPassword().equals(password)) {
				User user = temp.get();
				if (user.getEmail().equals("admin")) {
					currentSession.setAttribute("loginType", "Admin");
				}
				currentSession.setAttribute("currentUser", user);
				return user;
			}
		}
		return null;
	}



	@PostMapping("/api/user/admin")
	public User userCreationByAdmin(@RequestBody User user) throws JSONException {
		Optional<User> temp = ur.findById(user.getId());
		if (!temp.isPresent()) {
			user.setCreated(new Date());
			return ur.save(user);
		}
		return null;
	}

	@PutMapping("/api/user/update")
	public User updateUser(@RequestBody User currentUser, HttpSession currentSession) throws JSONException {
		User user = (User) currentSession.getAttribute("currentUser");
		Optional<User> temp = ur.findById(currentUser.getId());
		if(temp.isPresent()) {
			user.setName(currentUser.getName());
			user.setRottenUrl(currentUser.getRottenUrl());
			user.setImdbUrl(currentUser.getImdbUrl());
			return ur.save(user);
		}
		return null;
	}
	
	@PutMapping("/api/user/admin/update")
	public User updateUserByAdmin(@RequestBody User currentUser, HttpSession currentSession) throws JSONException {
		Optional<User> temp = ur.findById(currentUser.getId());
		if(temp.isPresent()) {
			User user = ur.findById(currentUser.getId()).get();
			user.setName(currentUser.getName());
			user.setRottenUrl(currentUser.getRottenUrl());
			user.setImdbUrl(currentUser.getImdbUrl());
			return ur.save(user);
		}
		return null;
	}

	@PutMapping("/api/user/update/{id}")
	public User updateUser(@RequestBody User currentUser, @PathVariable("id") int id) {
		Optional<User> temp = ur.findById(id);
		if(temp.isPresent()) {
			return ur.save(currentUser);
		}
		return null;
	}

	@GetMapping("/api/user/logout")
	public void logout(HttpSession currentSession) {
		if (currentSession.getId() != null)
			currentSession.invalidate();
	}

	@GetMapping("/api/loginType")
	public ResponseEntity<String> loginType(HttpSession currentSession) {
		JSONObject jsonBody = new JSONObject("{}");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (currentSession.getAttribute("loginType") == null) {
			jsonBody.put("loginType", "None");
			return new ResponseEntity<String>(jsonBody.toString(), headers, HttpStatus.ACCEPTED);
		}
		else {
			jsonBody.put("loginType", (String) currentSession.getAttribute("loginType"));
			return new ResponseEntity<String>(jsonBody.toString(), headers, HttpStatus.ACCEPTED);
		}
	}
}
