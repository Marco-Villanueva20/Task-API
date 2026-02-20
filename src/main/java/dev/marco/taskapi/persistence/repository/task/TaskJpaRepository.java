package dev.marco.taskapi.persistence.repository.task;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.marco.taskapi.persistence.entity.TaskEntity;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long>
{
	List<TaskEntity> findAllByUserId(Long userId);
	Optional<TaskEntity> findByIdAndUserId(Long id, Long userId);
	void deleteByIdAndUserId(Long id, Long userId);
}
