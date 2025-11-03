package br.ifsp.scrumou.task;

import br.ifsp.scrumou.controller.TaskController;
import br.ifsp.scrumou.dto.task.TaskRequestDTO;
import br.ifsp.scrumou.dto.task.TaskResponseDTO;
import br.ifsp.scrumou.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskControllerUnitTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    public void testGetAllTasks_ReturnsTasks() {
        Pageable pageable = PageRequest.of(0, 10);
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(1L);
        taskResponseDTO.setTitle("Test Task");
        taskResponseDTO.setDescription("Description");
        taskResponseDTO.setStatus("TO_DO");

        List<TaskResponseDTO> tasks = Collections.singletonList(taskResponseDTO);
        Page<TaskResponseDTO> page = new PageImpl<>(tasks, pageable, tasks.size());

        when(taskService.findAll(pageable)).thenReturn(page);

        ResponseEntity<Page<TaskResponseDTO>> response = taskController.getAllTasks(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    public void testNewTask_ReturnsCreatedTask() {
        TaskRequestDTO taskRequest = new TaskRequestDTO();
        taskRequest.setTitle("New Task");
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        taskResponse.setTitle("New Task");

        when(taskService.createTask(taskRequest)).thenReturn(taskResponse);

        ResponseEntity<TaskResponseDTO> response = taskController.newTask(taskRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taskResponse, response.getBody());
    }

    @Test
    public void testAlterTaskStatus_ReturnsUpdatedTask() {
        Long taskId = 1L;
        String newStatus = "DONE";
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        taskResponse.setId(taskId);
        taskResponse.setStatus(newStatus);

        when(taskService.updateStatus(taskId, newStatus)).thenReturn(taskResponse);

        ResponseEntity<TaskResponseDTO> response = taskController.alterTaskStatus(taskId, newStatus);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskResponse, response.getBody());
    }

    @Test
    public void testAlterTask_ReturnsUpdatedTask() {
        Long taskId = 1L;
        Map<String, String> updateTask = new HashMap<>();
        updateTask.put("title", "Updated Title");
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        taskResponse.setId(taskId);
        taskResponse.setTitle("Updated Title");

        when(taskService.updatePartial(taskId, updateTask)).thenReturn(taskResponse);

        ResponseEntity<TaskResponseDTO> response = taskController.alterTask(taskId, updateTask);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskResponse, response.getBody());
    }

    @Test
    public void testDeleteTask_ReturnsNoContent() {
        Long taskId = 1L;
        ResponseEntity<Void> response = taskController.deleteTask(taskId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetTaskById_ReturnsTask() {
        Long taskId = 1L;
        TaskResponseDTO taskResponse = new TaskResponseDTO();
        taskResponse.setId(taskId);

        when(taskService.findById(taskId)).thenReturn(taskResponse);

        ResponseEntity<TaskResponseDTO> response = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskResponse, response.getBody());
    }
}
