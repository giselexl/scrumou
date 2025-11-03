package br.ifsp.scrumou.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.scrumou.dto.sprint.SprintRequestDTO;
import br.ifsp.scrumou.dto.sprint.SprintResponseDTO;
import br.ifsp.scrumou.service.SprintService;

@RestController
@RequestMapping("api/sprints")
public class SprintController {
    @Autowired
    private SprintService sprintService;
    
    @PostMapping
    public ResponseEntity<SprintResponseDTO> newSprint(@RequestBody SprintRequestDTO sprint) {
        SprintResponseDTO createdSprint = sprintService.createSprint(sprint);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSprint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        sprintService.deleteSprint(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<SprintResponseDTO>> getAllSprints(Pageable pageable) {
        Page<SprintResponseDTO> sprints = sprintService.findAll(pageable);
        return ResponseEntity.ok(sprints);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintResponseDTO> getSprintById(@PathVariable Long id) {
        SprintResponseDTO sprint = sprintService.findById(id);
        return ResponseEntity.ok(sprint);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SprintResponseDTO> alterSprint(@PathVariable Long id, @RequestBody Map<String, String> updateSprint) {
        SprintResponseDTO updatedSprint = sprintService.updateSprint(id, updateSprint);
        return ResponseEntity.ok(updatedSprint);
    }
}
