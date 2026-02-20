package dev.marco.taskapi.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.marco.taskapi.domain.model.Task;

public interface TaskRepository {

	List<Task> findAll(Long userId);

	Optional<Task> findByIdAndUserId(Long id, Long userId);

	Task save(Task task);

	void deleteByIdAndUserId(Long id, Long userId);

	//Task save(Task task, Principal principal);
	
	//Task update(Task task, Long id);
}
