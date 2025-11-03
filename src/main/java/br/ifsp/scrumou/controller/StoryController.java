package br.ifsp.scrumou.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.ifsp.scrumou.dto.story.StoryRequestDTO;
import br.ifsp.scrumou.dto.story.StoryResponseDTO;
import br.ifsp.scrumou.service.StoryService;

@RestController
@RequestMapping("api/stories")
public class StoryController {
    @Autowired
    private StoryService storyService;

    @PostMapping
    public ResponseEntity<StoryResponseDTO> newStory(@RequestBody StoryRequestDTO story) {
        StoryResponseDTO createdStory = storyService.createStory(story);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStory);
    }

    @PatchMapping("/{id}/{newPriority}")
    public ResponseEntity<StoryResponseDTO> alterPriorityStory(@PathVariable Long id, @PathVariable Integer newPriority) {
        StoryResponseDTO updatedPriorityStory = storyService.alterPriority(id, newPriority);
        return ResponseEntity.ok(updatedPriorityStory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StoryResponseDTO> alterStory(@PathVariable Long id, @RequestBody Map<String, String> updateStory) {
        StoryResponseDTO updatedStory = storyService.updateStory(id, updateStory);
        return ResponseEntity.ok(updatedStory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<StoryResponseDTO>> getAllStories(Pageable pageable) {
        Page<StoryResponseDTO> stories = storyService.findAll(pageable);
        return ResponseEntity.ok(stories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryResponseDTO> getStoryById(@PathVariable Long id) {
        Optional<StoryResponseDTO> story = storyService.findById(id);

        return story.isPresent() ? ResponseEntity.ok(story.get()) : ResponseEntity.notFound().build();
    }
}