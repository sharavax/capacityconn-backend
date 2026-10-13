package com.example.capacity_connect.controller;

import com.example.capacity_connect.dto.TrainingProgramRequest;
import com.example.capacity_connect.dto.TrainingProgramResponse;
import com.example.capacity_connect.exception.ResourceNotFoundException;
import com.example.capacity_connect.service.TrainingProgramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingProgramControllerTest {

    @Mock
    private TrainingProgramService trainingProgramService;

    @InjectMocks
    private TrainingProgramController controller;

    private TrainingProgramRequest sampleRequest;
    private TrainingProgramResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleRequest = new TrainingProgramRequest(
                "AWS Cloud Practitioner",
                "Learn AWS cloud essentials",
                "4 weeks",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 28),
                "ACTIVE"
        );

        sampleResponse = new TrainingProgramResponse(
                1L,
                "AWS Cloud Practitioner",
                "Learn AWS cloud essentials",
                "4 weeks",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 28),
                "ACTIVE"
        );
    }

    @Test
    @DisplayName("createTrainingProgram returns 201 Created and response DTO")
    void createTrainingProgram_ReturnsCreated() {
        when(trainingProgramService.createTrainingProgram(any(TrainingProgramRequest.class)))
                .thenReturn(sampleResponse);

        ResponseEntity<TrainingProgramResponse> response = controller.createTrainingProgram(sampleRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getTitle()).isEqualTo("AWS Cloud Practitioner");
        verify(trainingProgramService, times(1)).createTrainingProgram(any(TrainingProgramRequest.class));
    }

    @Test
    @DisplayName("getAllTrainingPrograms returns 200 OK and list of programs")
    void getAllTrainingPrograms_ReturnsOkList() {
        when(trainingProgramService.getAllTrainingPrograms())
                .thenReturn(List.of(sampleResponse));

        ResponseEntity<List<TrainingProgramResponse>> response = controller.getAllTrainingPrograms();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("AWS Cloud Practitioner");
        verify(trainingProgramService, times(1)).getAllTrainingPrograms();
    }

    @Test
    @DisplayName("getTrainingProgramById returns 200 OK when program exists")
    void getTrainingProgramById_ReturnsProgram() {
        when(trainingProgramService.getTrainingProgramById(1L)).thenReturn(sampleResponse);

        ResponseEntity<TrainingProgramResponse> response = controller.getTrainingProgramById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        verify(trainingProgramService, times(1)).getTrainingProgramById(1L);
    }

    @Test
    @DisplayName("getTrainingProgramById throws ResourceNotFoundException when program does not exist")
    void getTrainingProgramById_NotFound() {
        when(trainingProgramService.getTrainingProgramById(99L))
                .thenThrow(new ResourceNotFoundException("TrainingProgram", "id", 99L));

        assertThatThrownBy(() -> controller.getTrainingProgramById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("TrainingProgram not found with id : '99'");

        verify(trainingProgramService, times(1)).getTrainingProgramById(99L);
    }

    @Test
    @DisplayName("updateTrainingProgram returns 200 OK and updated DTO")
    void updateTrainingProgram_ReturnsOk() {
        when(trainingProgramService.updateTrainingProgram(eq(1L), any(TrainingProgramRequest.class)))
                .thenReturn(sampleResponse);

        ResponseEntity<TrainingProgramResponse> response = controller.updateTrainingProgram(1L, sampleRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("AWS Cloud Practitioner");
        verify(trainingProgramService, times(1)).updateTrainingProgram(eq(1L), any(TrainingProgramRequest.class));
    }

    @Test
    @DisplayName("updateTrainingProgram throws ResourceNotFoundException when program does not exist")
    void updateTrainingProgram_NotFound() {
        when(trainingProgramService.updateTrainingProgram(eq(99L), any(TrainingProgramRequest.class)))
                .thenThrow(new ResourceNotFoundException("TrainingProgram", "id", 99L));

        assertThatThrownBy(() -> controller.updateTrainingProgram(99L, sampleRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(trainingProgramService, times(1)).updateTrainingProgram(eq(99L), any(TrainingProgramRequest.class));
    }

    @Test
    @DisplayName("deleteTrainingProgram returns 204 No Content")
    void deleteTrainingProgram_ReturnsNoContent() {
        doNothing().when(trainingProgramService).deleteTrainingProgram(1L);

        ResponseEntity<Void> response = controller.deleteTrainingProgram(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(trainingProgramService, times(1)).deleteTrainingProgram(1L);
    }

    @Test
    @DisplayName("deleteTrainingProgram throws ResourceNotFoundException when id does not exist")
    void deleteTrainingProgram_NotFound() {
        doThrow(new ResourceNotFoundException("TrainingProgram", "id", 99L))
                .when(trainingProgramService).deleteTrainingProgram(99L);

        assertThatThrownBy(() -> controller.deleteTrainingProgram(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(trainingProgramService, times(1)).deleteTrainingProgram(99L);
    }
}
