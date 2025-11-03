package br.ifsp.scrumou.service;

import br.ifsp.scrumou.dto.story.StoryRequestDTO;
import br.ifsp.scrumou.dto.story.StoryResponseDTO;
import br.ifsp.scrumou.model.Story;
import br.ifsp.scrumou.repository.StoryRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoryService {

    private final StoryRepository storyRepository;

    public StoryService(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public StoryResponseDTO createStory(StoryRequestDTO requestDTO) {

        Story story = mapToEntity(requestDTO);

        Story savedStory = storyRepository.save(story);

        return mapToResponseDTO(savedStory);
    }

    public List<StoryResponseDTO> findAll() {

        List<Story> stories = storyRepository.findAll();

        return stories.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteStory(Long id) {
        if (!storyRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "História  " + id + " não encontrada.");
        }
        storyRepository.deleteById(id);
    }

    public StoryResponseDTO alterPriority(Long id, Integer newPriority) {

        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "História com ID " + id + " não encontrada."));

        story.setPriority(newPriority);
        Story updatedStory = storyRepository.save(story);
        return mapToResponseDTO(updatedStory);
    }

    private Story mapToEntity(StoryRequestDTO dto) {
        Story story = new Story();

        story.setTitle(dto.getTitle());
        story.setDescription(dto.getDescription());
        story.setPriority(dto.getPriority());
        return story;
    }

    private StoryResponseDTO mapToResponseDTO(Story story) {
        StoryResponseDTO dto = new StoryResponseDTO();
        dto.setId(story.getId());
        dto.setTitle(story.getTitle());
        dto.setDescription(story.getDescription());
        dto.setPriority(story.getPriority());
        return dto;
    }

}
