package br.ifsp.scrumou.meeting;

import br.ifsp.scrumou.controller.MeetingController;
import br.ifsp.scrumou.dto.meeting.MeetingRequestDTO;
import br.ifsp.scrumou.dto.meeting.MeetingResponseDTO;
import br.ifsp.scrumou.service.MeetingService;
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
public class MeetingControllerUnitTest {

    @Mock
    private MeetingService meetingService;

    @InjectMocks
    private MeetingController meetingController;

    @Test
    public void testNewMeeting_ReturnsCreatedMeeting() {
        MeetingRequestDTO meetingRequest = new MeetingRequestDTO();
        MeetingResponseDTO meetingResponse = new MeetingResponseDTO();

        when(meetingService.createMeeting(meetingRequest)).thenReturn(meetingResponse);

        ResponseEntity<MeetingResponseDTO> response = meetingController.newMeeting(meetingRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(meetingResponse, response.getBody());
    }

    @Test
    public void testGetAllMeetings_ReturnsMeetings() {
        Pageable pageable = PageRequest.of(0, 10);
        MeetingResponseDTO meetingResponseDTO = new MeetingResponseDTO();
        meetingResponseDTO.setId(1L);

        List<MeetingResponseDTO> meetings = Collections.singletonList(meetingResponseDTO);
        Page<MeetingResponseDTO> page = new PageImpl<>(meetings, pageable, meetings.size());

        when(meetingService.findAll(pageable)).thenReturn(page);

        ResponseEntity<Page<MeetingResponseDTO>> response = meetingController.getAllMeetings(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    public void testGetMeetingById_ReturnsMeeting() {
        Long meetingId = 1L;
        MeetingResponseDTO meetingResponse = new MeetingResponseDTO();
        meetingResponse.setId(meetingId);

        when(meetingService.findById(meetingId)).thenReturn(meetingResponse);

        ResponseEntity<MeetingResponseDTO> response = meetingController.getMeetingById(meetingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(meetingResponse, response.getBody());
    }

    @Test
    public void testGetMeetingsBySprintId_ReturnsMeetings() {
        Long sprintId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        MeetingResponseDTO meetingResponseDTO = new MeetingResponseDTO();
        meetingResponseDTO.setId(1L);

        List<MeetingResponseDTO> meetings = Collections.singletonList(meetingResponseDTO);
        Page<MeetingResponseDTO> page = new PageImpl<>(meetings, pageable, meetings.size());

        when(meetingService.findMeetingsBySprintId(sprintId, pageable)).thenReturn(page);

        ResponseEntity<Page<MeetingResponseDTO>> response = meetingController.getMeetingsBySprintId(sprintId, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    public void testAlterMeeting_ReturnsUpdatedMeeting() {
        Long meetingId = 1L;
        Map<String, String> updateMeeting = new HashMap<>();
        updateMeeting.put("title", "Updated Title");
        MeetingResponseDTO meetingResponse = new MeetingResponseDTO();
        meetingResponse.setId(meetingId);
        meetingResponse.setTitle("Updated Title");

        when(meetingService.updateMeeting(meetingId, updateMeeting)).thenReturn(meetingResponse);

        ResponseEntity<MeetingResponseDTO> response = meetingController.alterMeeting(meetingId, updateMeeting);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(meetingResponse, response.getBody());
    }

    @Test
    public void testDeleteMeeting_ReturnsNoContent() {
        Long meetingId = 1L;
        ResponseEntity<Void> response = meetingController.deleteMeeting(meetingId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
