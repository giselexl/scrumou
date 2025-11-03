package br.ifsp.scrumou.service;

import java.time.LocalDate;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.ifsp.scrumou.dto.meeting.MeetingRequestDTO;
import br.ifsp.scrumou.dto.meeting.MeetingResponseDTO;
import br.ifsp.scrumou.model.Meeting;
import br.ifsp.scrumou.repository.MeetingRepository;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MeetingService(MeetingRepository meetingRepository, ModelMapper modelMapper) {
        this.meetingRepository = meetingRepository;
        this.modelMapper = modelMapper;
    }

    public MeetingResponseDTO createMeeting(MeetingRequestDTO requestDTO) {
        Meeting meeting = modelMapper.map(requestDTO, Meeting.class);
        Meeting savedMeeting = meetingRepository.save(meeting);
        return modelMapper.map(savedMeeting, MeetingResponseDTO.class);
    }

    public Page<MeetingResponseDTO> findAll(Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.findAll(pageable);
        return meetings.map(meeting -> modelMapper.map(meeting, MeetingResponseDTO.class));
    }

    public MeetingResponseDTO findById(Long id) {
        return meetingRepository.findById(id)
                .map(meeting -> modelMapper.map(meeting, MeetingResponseDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Reunião com ID " + id + " não encontrada."));
    }

    public Page<MeetingResponseDTO> findMeetingsBySprintId(Long sprintId, Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.findBySprintId(sprintId, pageable);
        return meetings.map(meeting -> modelMapper.map(meeting, MeetingResponseDTO.class));
    }

    public void deleteMeeting(Long id) {
        if (!meetingRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Reunião com ID " + id + " não encontrada.");
        }
        meetingRepository.deleteById(id);
    }

    public MeetingResponseDTO updateMeeting(Long id, Map<String, String> updateMeeting) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reunião com ID " + id + " não encontrada para alteração."));

        if (updateMeeting.containsKey("title")) {
            meeting.setTitle(updateMeeting.get("title"));
        }
        if (updateMeeting.containsKey("minutes")) {
            meeting.setMinutes(updateMeeting.get("minutes"));
        }
        if (updateMeeting.containsKey("date")) {
            meeting.setDate(LocalDate.parse(updateMeeting.get("date")));
        }
        if (updateMeeting.containsKey("time")) {
            meeting.setTime(updateMeeting.get("time"));
        }
        Meeting updatedMeeting = meetingRepository.save(meeting);
        return modelMapper.map(updatedMeeting, MeetingResponseDTO.class);
    }
}
