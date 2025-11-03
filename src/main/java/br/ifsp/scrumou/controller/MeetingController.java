package br.ifsp.scrumou.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.scrumou.dto.meeting.MeetingRequestDTO;
import br.ifsp.scrumou.dto.meeting.MeetingResponseDTO;
import br.ifsp.scrumou.service.MeetingService;

@RestController
@RequestMapping("api/meetings")
public class MeetingController{
    @Autowired
    private MeetingService meetingService;

    @PostMapping
    public ResponseEntity<MeetingResponseDTO> newMeeting(@RequestBody MeetingRequestDTO meeting) {
        MeetingResponseDTO createdMeeting = meetingService.createMeeting(meeting);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeeting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id) {
        meetingService.deleteMeeting(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<MeetingResponseDTO>> getAllMeetings(Pageable pageable) {
        Page<MeetingResponseDTO> meetings = meetingService.findAll(pageable);
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponseDTO> getMeetingById(@PathVariable Long id) {
        MeetingResponseDTO meeting = meetingService.findById(id);
        return ResponseEntity.ok(meeting);
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<Page<MeetingResponseDTO>> getMeetingsBySprintId(@PathVariable Long sprintId, Pageable pageable) {
        Page<MeetingResponseDTO> meetings = meetingService.findMeetingsBySprintId(sprintId, pageable);
        return ResponseEntity.ok(meetings);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MeetingResponseDTO> alterMeeting(@PathVariable Long id, @RequestBody Map<String, String> updateMeeting) {
        MeetingResponseDTO updatedMeeting = meetingService.updateMeeting(id, updateMeeting);
        return ResponseEntity.ok(updatedMeeting);
    }
}
