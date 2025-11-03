package br.ifsp.scrumou.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ifsp.scrumou.dto.task.TaskRequestDTO;
import br.ifsp.scrumou.dto.task.TaskResponseDTO;
import br.ifsp.scrumou.service.TaskService;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> newTask(@RequestBody TaskRequestDTO task) {
        TaskResponseDTO createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PatchMapping("/{id}/{newStatus}")
    public ResponseEntity<TaskResponseDTO> alterTaskStatus(@PathVariable Long id, @PathVariable String newStatus) {
        TaskResponseDTO updatedTask = taskService.updateStatus(id, newStatus);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> alterTask(@PathVariable Long id, @RequestBody Map<String, String> updateTask) {
        TaskResponseDTO updatedTask = taskService.updatePartial(id, updateTask);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(Pageable pageable) {
        Page<TaskResponseDTO> tasks = taskService.findAll(pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }
}