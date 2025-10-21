package br.ifsp.scrumou.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.ifsp.scrumou.model.Task;
import br.ifsp.scrumou.repository.TaskRepository;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    // Criar uma nova tarefa
    @PostMapping
    public Task newTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    // Alterar status da tarefa
    @PatchMapping("/{id}/{newStatus}")
    public Task alterTaskStatus(@PathVariable Long id, @PathVariable String newStatus) {
        Task task = taskRepository.findById(id).orElse(null);

        task.setStatus(newStatus);

        Task updatedTask = taskRepository.save(task);

        return updatedTask;
    }

    // Alterar uma tarefa existente
    @PatchMapping("/{id}")
    public Task alterTask(@PathVariable Long id, @RequestBody Map<String, String> updateTask) {
        Task task = taskRepository.findById(id).orElseThrow(null);
    
        updateTask.forEach((key, value) -> {
            switch (key) {
                case "title":
                    task.setTitle(value);
                    break;
                case "description":
                    task.setDescription(value);
                    break;
                case "hourEstimated":
                    task.setHourEstimated(Integer.parseInt(value));
                    break;
                case "developer":
                    task.setDeveloper(value);
                    break;
                case "status":
                    task.setStatus(value);
                    break;
            }
        });

        return taskRepository.save(task);
    }

    // Deletar uma tarefa
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }

    // Visualizar todas as tarefas
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Visualizar apenas uma tarefa
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id).orElseThrow(null);
    }
}