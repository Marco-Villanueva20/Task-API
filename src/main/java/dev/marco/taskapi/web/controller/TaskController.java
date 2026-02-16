package dev.marco.taskapi.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import dev.marco.taskapi.domain.model.Task;
import dev.marco.taskapi.domain.service.TaskService;
import dev.marco.taskapi.web.TaskWebMapper;
import dev.marco.taskapi.web.dto.TaskRequest;
import dev.marco.taskapi.web.dto.TaskResponse;
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
	public ResponseEntity<Task> createTask(@RequestBody TaskRequest request, UriComponentsBuilder ucb) {
		
		Task task = taskService.createTask(taskWebMapper.toDomain(request));
		URI location = ucb.cloneBuilder().path("/api/tasks/{id}").buildAndExpand(task.id()).toUri();
		return ResponseEntity.created(location).body(task);
	}
}
