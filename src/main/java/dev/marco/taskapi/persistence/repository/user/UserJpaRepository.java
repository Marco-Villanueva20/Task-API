package dev.marco.taskapi.persistence.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.marco.taskapi.persistence.entity.UserEntity;

public interface UserJpaRepository  extends JpaRepository<UserEntity, Long>{
	Optional<UserEntity> findByUsername(String username);
}
