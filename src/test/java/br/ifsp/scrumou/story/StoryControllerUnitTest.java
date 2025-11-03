package br.ifsp.scrumou.story;

import br.ifsp.scrumou.controller.StoryController;
import br.ifsp.scrumou.dto.story.StoryRequestDTO;
import br.ifsp.scrumou.dto.story.StoryResponseDTO;
import br.ifsp.scrumou.service.StoryService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoryControllerUnitTest {

    @Mock
    private StoryService storyService;

    @InjectMocks
    private StoryController storyController;

    @Test
    public void testNewStory_ReturnsCreatedStory() {
        StoryRequestDTO storyRequest = new StoryRequestDTO();
        storyRequest.setTitle("New Story");
        StoryResponseDTO storyResponse = new StoryResponseDTO();
        storyResponse.setTitle("New Story");

        when(storyService.createStory(storyRequest)).thenReturn(storyResponse);

        ResponseEntity<StoryResponseDTO> response = storyController.newStory(storyRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(storyResponse, response.getBody());
    }

    @Test
    public void testGetAllStories_ReturnsStories() {
        Pageable pageable = PageRequest.of(0, 10);
        StoryResponseDTO storyResponseDTO = new StoryResponseDTO();
        storyResponseDTO.setId(1L);
        storyResponseDTO.setTitle("Test Story");
        storyResponseDTO.setDescription("Description");
        storyResponseDTO.setPriority(1);

        List<StoryResponseDTO> stories = Collections.singletonList(storyResponseDTO);
        Page<StoryResponseDTO> page = new PageImpl<>(stories, pageable, stories.size());

        when(storyService.findAll(pageable)).thenReturn(page);

        ResponseEntity<Page<StoryResponseDTO>> response = storyController.getAllStories(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    public void testGetStoryById_ReturnsStory() {
        Long storyId = 1L;
        StoryResponseDTO storyResponse = new StoryResponseDTO();
        storyResponse.setId(storyId);

        when(storyService.findById(storyId)).thenReturn(Optional.of(storyResponse));

        ResponseEntity<StoryResponseDTO> response = storyController.getStoryById(storyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(storyResponse, response.getBody());
    }

    @Test
    public void testGetStoryById_ReturnsNotFound() {
        Long storyId = 1L;

        when(storyService.findById(storyId)).thenReturn(Optional.empty());

        ResponseEntity<StoryResponseDTO> response = storyController.getStoryById(storyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAlterPriorityStory_ReturnsUpdatedStory() {
        Long storyId = 1L;
        Integer newPriority = 2;
        StoryResponseDTO storyResponse = new StoryResponseDTO();
        storyResponse.setId(storyId);
        storyResponse.setPriority(newPriority);

        when(storyService.alterPriority(storyId, newPriority)).thenReturn(storyResponse);

        ResponseEntity<StoryResponseDTO> response = storyController.alterPriorityStory(storyId, newPriority);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(storyResponse, response.getBody());
    }

    @Test
    public void testAlterStory_ReturnsUpdatedStory() {
        Long storyId = 1L;
        Map<String, String> updateStory = new HashMap<>();
        updateStory.put("title", "Updated Title");
        StoryResponseDTO storyResponse = new StoryResponseDTO();
        storyResponse.setId(storyId);
        storyResponse.setTitle("Updated Title");

        when(storyService.updateStory(storyId, updateStory)).thenReturn(storyResponse);

        ResponseEntity<StoryResponseDTO> response = storyController.alterStory(storyId, updateStory);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(storyResponse, response.getBody());
    }

    @Test
    public void testDeleteStory_ReturnsNoContent() {
        Long storyId = 1L;
        ResponseEntity<Void> response = storyController.deleteStory(storyId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
