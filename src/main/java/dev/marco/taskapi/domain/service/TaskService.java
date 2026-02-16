package dev.marco.taskapi.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.marco.taskapi.domain.exception.ResourceNotFoundException;
import dev.marco.taskapi.domain.model.Task;
import dev.marco.taskapi.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public Task createTask(Task task) {
		return taskRepository.save(task);
	}

	public Task getTaskById(Long id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
	}

	public void deleteTaskById(Long id) {

		Task existingTask = taskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

		taskRepository.deleteById(existingTask.id());
	}

	public Task updateTask(Task task, Long id) {

	    Task existingTask = taskRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Task not found with id: " + id
	                    )
	            );

	    Task updatedTask = new Task(
	            existingTask.id(),
	            task.title(),
	            task.description()
	    );

	    return taskRepository.save(updatedTask);
	}

}
