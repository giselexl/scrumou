package br.ifsp.scrumou.service;

import br.ifsp.scrumou.dto.task.TaskRequestDTO;
import br.ifsp.scrumou.dto.task.TaskResponseDTO;
import br.ifsp.scrumou.model.Task;
import br.ifsp.scrumou.repository.TaskRepository;

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

    public TaskService(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
        Task task = modelMapper.map(requestDTO, Task.class);

        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            //TODO alter hardcoded status to enum default value
            task.setStatus("TO DO");
        }

        return modelMapper.map(taskRepository.save(task), TaskResponseDTO.class);
    }

    public Page<TaskResponseDTO> findAll(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);

        return tasks.map(task -> modelMapper.map(task, TaskResponseDTO.class));
    }

    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarefa com ID " + id + " não encontrada."));
        return modelMapper.map(task, TaskResponseDTO.class);
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

        return modelMapper.map(taskRepository.save(task), TaskResponseDTO.class);
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

        return modelMapper.map(taskRepository.save(task), TaskResponseDTO.class);
    }
}