package br.ifsp.scrumou.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(storyService.createStory(story));
    }

    @PatchMapping("/{id}/{newPriority}")
    public ResponseEntity<StoryResponseDTO> alterPriorityStory(@PathVariable Long id, @PathVariable Integer newPriority) {
        return ResponseEntity.ok(storyService.alterPriority(id, newPriority));
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
    public List<StoryResponseDTO> getAllStories() {
        return storyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryResponseDTO> getStoryById(@PathVariable Long id) {
        Optional<StoryResponseDTO> story = storyService.findById(id);

        return story.isPresent() ? ResponseEntity.ok(story.get()) : ResponseEntity.notFound().build();
    }
}