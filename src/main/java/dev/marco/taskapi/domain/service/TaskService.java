package dev.marco.taskapi.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.marco.taskapi.domain.exception.ResourceNotFoundException;
import dev.marco.taskapi.domain.model.Task;
import dev.marco.taskapi.domain.model.User;
import dev.marco.taskapi.domain.repository.TaskRepository;
import dev.marco.taskapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	public List<Task> getAllTasks(String username) {
		User user = getUserByUsername(username);
		
		return taskRepository.findAll(user.id());
	}

	@Transactional
	public Task createTask(Task task, String username) {
		
		User user = getUserByUsername(username);
		
		task.setUser(user);
		
		return taskRepository.save(task);
	}

	@Transactional
	public Task getTaskById(Long id, String username) {
		User user = getUserByUsername(username);
		return taskRepository.findByIdAndUserId(id, user.id())
				.orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
	}

	@Transactional
	public void deleteTaskById(Long id, String username) {
		User user = getUserByUsername(username);
		Task existingTask = taskRepository.findByIdAndUserId(id, user.id())
				.orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

		taskRepository.deleteByIdAndUserId(existingTask.getId(), user.id());
	}
	
	private User getUserByUsername(String username) {
			return userRepository.findByUsername(username)
			.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
	}
	
	
	@Transactional
	public Task updateTask(Long id, Task task, String username) {
		
		User user = getUserByUsername(username);

	    Task existingTask = taskRepository.findByIdAndUserId(id, user.id())
	    		.filter(t -> t.getUser().id().equals(user.id()))
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Task not found with id: " + id
	                    )
	            );
	    

	    Task updatedTask = new Task(
	            existingTask.getId(),
	            task.getTitle(),
	            task.getDescription(),
	            existingTask.getUser()
	    );

	    return taskRepository.save(updatedTask);
	}

}
