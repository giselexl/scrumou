package br.ifsp.scrumou.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.scrumou.model.Sprint;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    
}
