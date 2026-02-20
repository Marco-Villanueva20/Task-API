package dev.marco.taskapi.web.mapper;

import org.springframework.stereotype.Component;

import dev.marco.taskapi.domain.model.Task;
import dev.marco.taskapi.web.dto.TaskRequest;
import dev.marco.taskapi.web.dto.TaskResponse;

@Component
public record TaskWebMapper() {

	public TaskResponse toResponse(Task task) {
		return new TaskResponse(task.getId(), task.getTitle(), task.getDescription());
	}

	public Task toDomain(TaskRequest request) {
		return new Task(null, request.title(), request.description(), null);
	}
}
