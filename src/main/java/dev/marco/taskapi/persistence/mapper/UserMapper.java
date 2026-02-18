package dev.marco.taskapi.persistence.mapper;

import org.springframework.stereotype.Component;

import dev.marco.taskapi.domain.model.User;
import dev.marco.taskapi.persistence.entity.UserEntity;

@Component
public record UserMapper() {

	public UserEntity toEntity(User user) {
		return UserEntity.builder()
				.id(user.id())
				.username(user.username())
				.password(user.password())
				.email(user.email())
				.role(user.role())
				.build();
	}
	public User toDomain(UserEntity userEntity) {
		return new User(userEntity.getId(),
				userEntity.getUsername(),
				userEntity.getPassword(),
				userEntity.getEmail(),
				userEntity.getRole());
	}
	
}
