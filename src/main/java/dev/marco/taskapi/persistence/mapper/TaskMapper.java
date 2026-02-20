package dev.marco.taskapi.persistence.mapper;

import org.springframework.stereotype.Component;

import dev.marco.taskapi.domain.model.Task;
import dev.marco.taskapi.persistence.entity.TaskEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskMapper {

	private final UserMapper userMapper;

	public TaskEntity toEntity(Task task) {
	    return TaskEntity.builder()
	        .id(task.getId())
	        .title(task.getTitle())
	        .description(task.getDescription())
	        .build();
	}


	public Task toDomain(TaskEntity taskEntity) {
		return new Task(taskEntity.getId(), taskEntity.getTitle(), taskEntity.getDescription(),
				userMapper.toDomain(taskEntity.getUser()));
	}

}
