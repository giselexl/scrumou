package br.ifsp.scrumou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Map;

import org.modelmapper.ModelMapper;

import br.ifsp.scrumou.dto.sprint.SprintRequestDTO;
import br.ifsp.scrumou.dto.sprint.SprintResponseDTO;
import br.ifsp.scrumou.model.Sprint;
import br.ifsp.scrumou.model.Story;
import br.ifsp.scrumou.repository.SprintRepository;

@Service
public class SprintService {
    private final SprintRepository sprintRepository;

    @Autowired
    private ModelMapper modelMapper;

    public SprintService(SprintRepository sprintRepository, ModelMapper modelMapper) {
        this.sprintRepository = sprintRepository;
        this.modelMapper = modelMapper;
    }

    public SprintResponseDTO createSprint(SprintRequestDTO requestDTO) {
        Sprint sprint = modelMapper.map(requestDTO, Sprint.class);
        Sprint savedSprint = sprintRepository.save(sprint);

        return modelMapper.map(savedSprint, SprintResponseDTO.class);
    }

    public void deleteSprint(Long id) {
        if (!sprintRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Sprint com o ID " + id + " não encontrado para exclusão.");
        }
        sprintRepository.deleteById(id);
    }

    public SprintResponseDTO findById(Long id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sprint com o ID " + id + " não encontrado."));
        return modelMapper.map(sprint, SprintResponseDTO.class);
    }
    
    public Page<SprintResponseDTO> findAll(Pageable pageable) {
        Page<Sprint> sprints = sprintRepository.findAll(pageable);

        return sprints.map(sprint -> modelMapper.map(sprint, SprintResponseDTO.class));
    }

    public SprintResponseDTO updateSprint(Long id, Map<String, String> sprintUpdates) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sprint com o ID " + id + " não encontrado para atualização."));

        sprintUpdates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    sprint.setTitle(value);
                    break;
                case "startDate":
                    sprint.setStartDate(LocalDate.parse(value));
                    break;
                case "endDate":
                    sprint.setEndDate(LocalDate.parse(value));
                    break;
                case "story":
                    Story story = modelMapper.map(value, Story.class);
                    sprint.setStory(story);
                    break;
            }
        });

        Sprint updatedSprint = sprintRepository.save(sprint);
        return modelMapper.map(updatedSprint, SprintResponseDTO.class);
    }

}
