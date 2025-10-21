package br.ifsp.scrumou.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.ifsp.scrumou.model.Story;
import br.ifsp.scrumou.repository.StoryRepository;

@RestController
@RequestMapping("api/stories")
public class StoryController {
    @Autowired
    private StoryRepository storyRepository;

    // Criar uma nova história
    @PostMapping
    public Story newStory(@RequestBody Story story) {
        return storyRepository.save(story);
    }

    // Alterar ordem de prioridade
    @PatchMapping("/{id}/{newPriority}")
    public Story alterPriorityStory(@PathVariable Long id, @PathVariable Integer newPriority) {
        Story story = storyRepository.findById(id).orElse(null);

        story.setPriority(newPriority);

        Story updatedStory = storyRepository.save(story);

        return updatedStory;
    }

    // Alterar uma história existente
    @PatchMapping("/{id}")
    public Story alterStory(@PathVariable Long id, @RequestBody Map<String, String> updateStory) {
        Story story = storyRepository.findById(id).orElseThrow(null);
    
        updateStory.forEach((key, value) -> {
            switch (key) {
                case "title":
                    story.setTitle(value);
                    break;
                case "description":
                    story.setDescription(value);
                    break;
                case "priority":
                    story.setPriority(Integer.parseInt(value));
                    break;
            }
        });

        return storyRepository.save(story);
    }

    // Deletar uma historia
    @DeleteMapping("/{id}")
    public void deleteStory(@PathVariable Long id) {
        storyRepository.deleteById(id);
    }

    // Visualizar todas as histórias
    @GetMapping
    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }

    // Visualizar apenas uma história
    @GetMapping("/{id}")
    public Optional<Story> getStoryById(@PathVariable Long id) {
        return storyRepository.findById(id);
    }
}