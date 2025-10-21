package br.ifsp.scrumou.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.scrumou.model.Story;

public interface StoryRepository extends JpaRepository<Story, Long> {

}