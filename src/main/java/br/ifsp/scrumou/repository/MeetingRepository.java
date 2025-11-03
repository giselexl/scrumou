package br.ifsp.scrumou.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.scrumou.model.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Page<Meeting> findBySprintId(Long sprintId, Pageable pageable);
}