package dev.marco.taskapi.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.marco.taskapi.domain.model.Task;

public interface TaskRepository {

	List<Task> findAll();

	Optional<Task> findById(Long id);

	Task save(Task task);

	void deleteById(Long id);
	
	//Task update(Task task, Long id);
}
