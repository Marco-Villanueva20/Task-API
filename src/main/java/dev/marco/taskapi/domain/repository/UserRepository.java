package dev.marco.taskapi.domain.repository;

import dev.marco.taskapi.domain.model.User;

public interface UserRepository {
	
	User save(User user);
}
