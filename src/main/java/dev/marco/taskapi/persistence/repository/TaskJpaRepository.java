package dev.marco.taskapi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.marco.taskapi.persistence.entity.TaskEntity;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long>
{

}
