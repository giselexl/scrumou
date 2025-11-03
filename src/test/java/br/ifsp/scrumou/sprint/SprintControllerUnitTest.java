package br.ifsp.scrumou.sprint;

import br.ifsp.scrumou.controller.SprintController;
import br.ifsp.scrumou.dto.sprint.SprintRequestDTO;
import br.ifsp.scrumou.dto.sprint.SprintResponseDTO;
import br.ifsp.scrumou.service.SprintService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SprintControllerUnitTest {

    @Mock
    private SprintService sprintService;

    @InjectMocks
    private SprintController sprintController;

    @Test
    public void testNewSprint_ReturnsCreatedSprint() {
        SprintRequestDTO sprintRequest = new SprintRequestDTO();
        sprintRequest.setTitle("New Sprint");
        SprintResponseDTO sprintResponse = new SprintResponseDTO();
        sprintResponse.setTitle("New Sprint");

        when(sprintService.createSprint(sprintRequest)).thenReturn(sprintResponse);

        ResponseEntity<SprintResponseDTO> response = sprintController.newSprint(sprintRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sprintResponse, response.getBody());
    }

    @Test
    public void testGetAllSprints_ReturnsSprints() {
        Pageable pageable = PageRequest.of(0, 10);
        SprintResponseDTO sprintResponseDTO = new SprintResponseDTO();
        sprintResponseDTO.setId(1L);
        sprintResponseDTO.setTitle("Test Sprint");

        List<SprintResponseDTO> sprints = Collections.singletonList(sprintResponseDTO);
        Page<SprintResponseDTO> page = new PageImpl<>(sprints, pageable, sprints.size());

        when(sprintService.findAll(pageable)).thenReturn(page);

        ResponseEntity<Page<SprintResponseDTO>> response = sprintController.getAllSprints(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    public void testGetSprintById_ReturnsSprint() {
        Long sprintId = 1L;
        SprintResponseDTO sprintResponse = new SprintResponseDTO();
        sprintResponse.setId(sprintId);

        when(sprintService.findById(sprintId)).thenReturn(sprintResponse);

        ResponseEntity<SprintResponseDTO> response = sprintController.getSprintById(sprintId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sprintResponse, response.getBody());
    }

    @Test
    public void testAlterSprint_ReturnsUpdatedSprint() {
        Long sprintId = 1L;
        Map<String, String> updateSprint = new HashMap<>();
        updateSprint.put("title", "Updated Title");
        SprintResponseDTO sprintResponse = new SprintResponseDTO();
        sprintResponse.setId(sprintId);
        sprintResponse.setTitle("Updated Title");

        when(sprintService.updateSprint(sprintId, updateSprint)).thenReturn(sprintResponse);

        ResponseEntity<SprintResponseDTO> response = sprintController.alterSprint(sprintId, updateSprint);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sprintResponse, response.getBody());
    }

    @Test
    public void testDeleteSprint_ReturnsNoContent() {
        Long sprintId = 1L;
        ResponseEntity<Void> response = sprintController.deleteSprint(sprintId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
