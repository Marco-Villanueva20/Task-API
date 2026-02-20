package dev.marco.taskapi.domain.repository;

import java.util.Optional;

import dev.marco.taskapi.domain.model.User;

public interface UserRepository {
	
	User save(User user);

	Optional<User> findByUsername(String username);
}
