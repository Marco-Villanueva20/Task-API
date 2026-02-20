package dev.marco.taskapi.persistence.repository.task;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.marco.taskapi.domain.model.Task;
import dev.marco.taskapi.domain.repository.TaskRepository;
import dev.marco.taskapi.persistence.entity.TaskEntity;
import dev.marco.taskapi.persistence.entity.UserEntity;
import dev.marco.taskapi.persistence.mapper.TaskMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

	private final TaskJpaRepository taskJpaRepository;
	private final EntityManager entityManager;
	private final TaskMapper taskMapper;

	@Override
	public List<Task> findAll(Long userId) {
		return taskJpaRepository.findAllByUserId(userId).stream().map(taskEntity -> taskMapper.toDomain(taskEntity)).toList();
	}

	@Override
	public Optional<Task> findByIdAndUserId(Long id, Long userId) {
	    return taskJpaRepository.findByIdAndUserId(id, userId)
	            .map(taskMapper::toDomain);
	}
	
	

	@Override
	public Task save(Task task) {
		
		if (task.getUser() == null || task.getUser().id() == null) {
	        throw new IllegalArgumentException("Task.user and Task.user.id are required");
	    }

		
		
		UserEntity userRef  =  entityManager.getReference(UserEntity.class, task.getUser().id());
		TaskEntity taskEntity = taskMapper.toEntity(task);
		taskEntity.setUser(userRef);
		
		TaskEntity savedTaskEntity = taskJpaRepository.save(taskEntity);
		return taskMapper.toDomain(savedTaskEntity);
	}

	@Override
	public void deleteByIdAndUserId(Long id, Long userId) {
	    taskJpaRepository.deleteByIdAndUserId(id, userId);
	}

}
