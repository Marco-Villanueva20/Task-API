package dev.marco.taskapi.persistence.mapper;

import org.springframework.stereotype.Component;

import dev.marco.taskapi.domain.model.Task;
import dev.marco.taskapi.persistence.entity.TaskEntity;

@Component
public record TaskMapper() {
	public TaskEntity toEntity(Task task) {
		return TaskEntity.builder()
				.id(task.id())
				.title(task.title())
				.description(task.description())
				.build();
	}

	public Task toDomain(TaskEntity taskEntity) {
		return new Task(taskEntity.getId(),
				taskEntity.getTitle(),
				taskEntity.getDescription());
	}

}
