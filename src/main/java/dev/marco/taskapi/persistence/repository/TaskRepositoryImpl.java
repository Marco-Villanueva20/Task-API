package dev.marco.taskapi.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.marco.taskapi.domain.model.Task;
import dev.marco.taskapi.domain.repository.TaskRepository;
import dev.marco.taskapi.persistence.entity.TaskEntity;
import dev.marco.taskapi.persistence.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

	private final TaskJpaRepository taskJpaRepository;
	private final TaskMapper taskMapper;

	@Override
	public List<Task> findAll() {
		return taskJpaRepository.findAll().stream().map(taskEntity -> taskMapper.toDomain(taskEntity)).toList();
	}

	@Override
	public Optional<Task> findById(Long id) {
	    return taskJpaRepository.findById(id)
	            .map(taskMapper::toDomain);
	}
	

	@Override
	public Task save(Task task) {
		TaskEntity savedTaskEntity = taskJpaRepository.save(taskMapper.toEntity(task));
		return taskMapper.toDomain(savedTaskEntity);
	}

	@Override
	public void deleteById(Long id) {
	    taskJpaRepository.deleteById(id);
	}

}
