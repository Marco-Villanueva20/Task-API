package dev.marco.taskapi.web.controller;

import java.net.URI;
import java.security.Principal;
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
	public ResponseEntity<List<TaskResponse>> getAllTasks(Principal principal) {

		log.info("Getting all tasks");
		List<TaskResponse> responses = taskService.getAllTasks(principal.getName()).stream()
				.map(taskModel -> taskWebMapper.toResponse(taskModel)).toList();
		return ResponseEntity.ok(responses);
	}

	@PostMapping
	public ResponseEntity<TaskResponse> createTask(

			@Valid @RequestBody TaskRequest request, UriComponentsBuilder ucb, Principal principal) {
		String username = principal.getName();
		Task task = taskService.createTask(taskWebMapper.toDomain(request), username);
		URI location = ucb.cloneBuilder().path("/api/tasks/{id}").buildAndExpand(task.getId()).toUri();
		return ResponseEntity.created(location).body(taskWebMapper.toResponse(task));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TaskResponse> updateTask(@RequestBody TaskRequest request, @PathVariable Long id,
			Principal principal) {
		String username = principal.getName();
		Task taskDomain = taskWebMapper.toDomain(request);
		Task updatedTask = taskService.updateTask(id, taskDomain, username);
		return ResponseEntity.ok(taskWebMapper.toResponse(updatedTask));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTaskById(@PathVariable Long id, Principal principal) {
		taskService.deleteTaskById(id, principal.getName());
		return ResponseEntity.noContent().build();
	}
}
