package br.ifsp.scrumou.service;

import br.ifsp.scrumou.dto.task.TaskRequestDTO;
import br.ifsp.scrumou.dto.task.TaskResponseDTO;
import br.ifsp.scrumou.model.Task;
import br.ifsp.scrumou.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
        Task task = mapToEntity(requestDTO);

        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            task.setStatus("TO DO");
        }

        Task savedTask = taskRepository.save(task);
        return mapToResponseDTO(savedTask);
    }

    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa com ID " + id + " não encontrada."));
        return mapToResponseDTO(task);
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

        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }

    public TaskResponseDTO updatePartial(Long id, Map<String, String> updates) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa " + id + " não encontrada"));

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
                case "developer":
                    task.setDeveloper(value);
                    break;
                case "status":
                    task.setStatus(value);
                    break;
            }
        });

        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }

    private Task mapToEntity(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setHourEstimated(dto.getHourEstimated());
        task.setDeveloper(dto.getDeveloper());
        task.setStatus(dto.getStatus());
        return task;
    }

    private TaskResponseDTO mapToResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setHourEstimated(task.getHourEstimated());
        dto.setDeveloper(task.getDeveloper());
        dto.setStatus(task.getStatus());
        return dto;
    }
}