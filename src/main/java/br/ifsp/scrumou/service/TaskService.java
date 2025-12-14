package br.ifsp.scrumou.service;

import br.ifsp.scrumou.dto.task.TaskRequestDTO;
import br.ifsp.scrumou.dto.task.TaskResponseDTO;
import br.ifsp.scrumou.identity.dto.UserResponse;
import br.ifsp.scrumou.model.Task;
import br.ifsp.scrumou.repository.TaskRepository;
import br.ifsp.scrumou.identity.adapter.IdentityFacade;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final IdentityFacade identityFacade;

    public TaskService(TaskRepository taskRepository, ModelMapper modelMapper, IdentityFacade identityFacade) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
        this.identityFacade = identityFacade;
    }

    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
        Task task = modelMapper.map(requestDTO, Task.class);

        String devIdentifier = requestDTO.getDeveloper();
        if (devIdentifier != null && !devIdentifier.isBlank()) {
            UserResponse user = identityFacade.resolveDeveloper(devIdentifier);
            task.setDeveloperId(user.id);
            task.setDeveloper(user);
        }

        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            task.setStatus("TODO");
        }

        Task saved = taskRepository.save(task);

        if (saved.getDeveloperId() != null && saved.getDeveloper() == null) {
            saved.setDeveloper(identityFacade.resolveDeveloper(String.valueOf(saved.getDeveloperId())));
        }

        TaskResponseDTO dto = modelMapper.map(saved, TaskResponseDTO.class);

        if (saved.getDeveloper() != null) {
            dto.setDeveloper(saved.getDeveloper().name);
        }

        return dto;
    }

    public Page<TaskResponseDTO> findAll(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);

        return tasks.map(task -> {
            if (task.getDeveloperId() != null && task.getDeveloper() == null) {
                task.setDeveloper(identityFacade.resolveDeveloper(String.valueOf(task.getDeveloperId())));
            }
            TaskResponseDTO dto = modelMapper.map(task, TaskResponseDTO.class);
            if (task.getDeveloper() != null) {
                dto.setDeveloper(task.getDeveloper().name);
            }
            return dto;
        });
    }

    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa com ID " + id + " não encontrada."));
        if (task.getDeveloperId() != null) {
            task.setDeveloper(identityFacade.resolveDeveloper(String.valueOf(task.getDeveloperId())));
        }
        TaskResponseDTO dto = modelMapper.map(task, TaskResponseDTO.class);
        if (task.getDeveloper() != null) {
            dto.setDeveloper(task.getDeveloper().name);
        }
        return dto;
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Tarefa com ID " + id + " não encontrada para exclusão.");
        }
        taskRepository.deleteById(id);
    }

    public TaskResponseDTO updateStatus(Long id, String newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa com ID " + id + " não encontrada para alteração."));

        task.setStatus(newStatus);

        Task saved = taskRepository.save(task);
        TaskResponseDTO dto = modelMapper.map(saved, TaskResponseDTO.class);
        if (saved.getDeveloper() != null) {
            dto.setDeveloper(saved.getDeveloper().name);
        }
        return dto;
    }

    public TaskResponseDTO updatePartial(Long id, Map<String, String> updates) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa com ID " + id + " não encontrada para alteração."));

        if (updates.containsKey("developer")) {
            String devIdentifier = updates.get("developer");
            UserResponse user = identityFacade.resolveDeveloper(devIdentifier);
            task.setDeveloperId(user.id);
            task.setDeveloper(user);
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    task.setTitle(value);
                    break;
                case "description":
                    task.setDescription(value);
                    break;
                case "hourEstimated":
                    try {
                        task.setHourEstimated(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "hourEstimated deve ser um número inteiro.");
                    }
                    break;
                case "status":
                    task.setStatus(value);
                    break;
            }
        });
        Task saved = taskRepository.save(task);
        TaskResponseDTO dto = modelMapper.map(saved, TaskResponseDTO.class);
        if (saved.getDeveloper() != null) {
            dto.setDeveloper(saved.getDeveloper().name);
        }
        return dto;
    }
}