package dev.marco.taskapi.persistence.repository.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.marco.taskapi.domain.model.User;
import dev.marco.taskapi.domain.repository.UserRepository;
import dev.marco.taskapi.persistence.entity.UserEntity;
import dev.marco.taskapi.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;
	private final UserMapper userMapper;

	@Override
	public User save(User user) {
		UserEntity userEntity = userMapper.toEntity(user);

		UserEntity savedEntity = userJpaRepository.save(userEntity);
		return userMapper.toDomain(savedEntity);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userJpaRepository.findByUsername(username)
				.map(userMapper::toDomain);
	}
	

}
