package br.ifsp.scrumou.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.scrumou.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}