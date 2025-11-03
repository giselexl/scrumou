package br.ifsp.scrumou.service;

import br.ifsp.scrumou.dto.story.StoryRequestDTO;
import br.ifsp.scrumou.dto.story.StoryResponseDTO;
import br.ifsp.scrumou.model.Story;
import br.ifsp.scrumou.repository.StoryRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

@Service
public class StoryService {

    private final StoryRepository storyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StoryService(StoryRepository storyRepository, ModelMapper modelMapper) {
        this.storyRepository = storyRepository;
        this.modelMapper = modelMapper;
    }

    public StoryResponseDTO createStory(StoryRequestDTO requestDTO) {
        Story story = modelMapper.map(requestDTO, Story.class);
        Story savedStory = storyRepository.save(story);
        return modelMapper.map(savedStory, StoryResponseDTO.class);
    }

    public Page<StoryResponseDTO> findAll(Pageable pageable) {
        Page<Story> stories = storyRepository.findAll(pageable);
        return stories.map(story -> modelMapper.map(story, StoryResponseDTO.class));
    }

    public Optional<StoryResponseDTO> findById(Long id) {
        if (!storyRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "História com ID " + id + " não encontrada.");
        }

        return storyRepository.findById(id)
                .map(story -> modelMapper.map(story, StoryResponseDTO.class));
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
        return modelMapper.map(updatedStory, StoryResponseDTO.class);
    }

    public StoryResponseDTO updateStory(Long id, Map<String, String> updateStory) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "História com ID " + id + " não encontrada para alteração."));

        if (updateStory.containsKey("title")) {
            story.setTitle(updateStory.get("title"));
        }
        if (updateStory.containsKey("description")) {
            story.setDescription(updateStory.get("description"));
        }
        if (updateStory.containsKey("priority")) {
            story.setPriority(Integer.parseInt(updateStory.get("priority")));
        }

        Story updatedStory = storyRepository.save(story);
        return modelMapper.map(updatedStory, StoryResponseDTO.class);
    }

}
