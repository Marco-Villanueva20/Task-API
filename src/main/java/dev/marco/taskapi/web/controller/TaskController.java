package dev.marco.taskapi.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dev.marco.taskapi.domain.model.Task;
import dev.marco.taskapi.domain.service.TaskService;
import dev.marco.taskapi.web.dto.TaskRequest;
import dev.marco.taskapi.web.dto.TaskResponse;
import dev.marco.taskapi.web.mapper.TaskWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;
	private final TaskWebMapper taskWebMapper;

	@GetMapping
	public ResponseEntity<List<TaskResponse>> getAllTasks() {
		log.info("Getting all tasks");
		List<TaskResponse> responses = taskService.getAllTasks().stream().map(taskModel -> taskWebMapper.toResponse(taskModel)).toList();
		return ResponseEntity.ok(responses);
	}

	@PostMapping
	public ResponseEntity<TaskResponse> createTask(
			
			@Valid @RequestBody TaskRequest request, UriComponentsBuilder ucb) {
		
		Task task = taskService.createTask(taskWebMapper.toDomain(request));
		URI location = ucb.cloneBuilder().path("/api/tasks/{id}").buildAndExpand(task.id()).toUri();
		return ResponseEntity.created(location).body(taskWebMapper.toResponse(task));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TaskResponse> updateTask(@RequestBody TaskRequest request, @PathVariable Long id){
		Task taskDomain = taskWebMapper.toDomain(request);
		Task updatedTask = taskService.updateTask(id, taskDomain);
		return ResponseEntity.ok(taskWebMapper.toResponse(updatedTask));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
		taskService.deleteTaskById(id);
		return ResponseEntity.noContent().build();
	}
}
