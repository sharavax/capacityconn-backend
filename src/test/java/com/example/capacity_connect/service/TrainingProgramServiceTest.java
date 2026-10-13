package com.example.capacity_connect.service;

import com.example.capacity_connect.dto.TrainingProgramRequest;
import com.example.capacity_connect.dto.TrainingProgramResponse;
import com.example.capacity_connect.exception.ResourceNotFoundException;
import com.example.capacity_connect.model.TrainingProgram;
import com.example.capacity_connect.repository.TrainingProgramRepository;
import com.example.capacity_connect.service.impl.TrainingProgramServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingProgramServiceTest {

    @Mock
    private TrainingProgramRepository repository;

    @InjectMocks
    private TrainingProgramServiceImpl service;

    private TrainingProgram sampleEntity;
    private TrainingProgramRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleEntity = new TrainingProgram(
                1L,
                "Full Stack Java Bootcamp",
                "Comprehensive Java and React course",
                "12 weeks",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2027, 1, 24),
                "ACTIVE"
        );

        sampleRequest = new TrainingProgramRequest(
                "Full Stack Java Bootcamp",
                "Comprehensive Java and React course",
                "12 weeks",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2027, 1, 24),
                "ACTIVE"
        );
    }

    @Test
    @DisplayName("createTrainingProgram successfully saves and returns response DTO")
    void createTrainingProgram_Success() {
        when(repository.save(any(TrainingProgram.class))).thenReturn(sampleEntity);

        TrainingProgramResponse response = service.createTrainingProgram(sampleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Full Stack Java Bootcamp");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
        verify(repository, times(1)).save(any(TrainingProgram.class));
    }

    @Test
    @DisplayName("createTrainingProgram throws IllegalArgumentException when endDate is before startDate")
    void createTrainingProgram_InvalidDates_ThrowsException() {
        sampleRequest.setStartDate(LocalDate.of(2026, 12, 1));
        sampleRequest.setEndDate(LocalDate.of(2026, 11, 1));

        assertThatThrownBy(() -> service.createTrainingProgram(sampleRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("End date cannot be earlier than start date");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("getAllTrainingPrograms returns all training programs as DTO list")
    void getAllTrainingPrograms_Success() {
        when(repository.findAll()).thenReturn(List.of(sampleEntity));

        List<TrainingProgramResponse> list = service.getAllTrainingPrograms();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getTitle()).isEqualTo("Full Stack Java Bootcamp");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllTrainingPrograms returns empty list when no records exist")
    void getAllTrainingPrograms_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<TrainingProgramResponse> list = service.getAllTrainingPrograms();

        assertThat(list).isEmpty();
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("getTrainingProgramById returns program when id exists")
    void getTrainingProgramById_Found() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEntity));

        TrainingProgramResponse response = service.getTrainingProgramById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Full Stack Java Bootcamp");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getTrainingProgramById throws ResourceNotFoundException when id does not exist")
    void getTrainingProgramById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTrainingProgramById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("TrainingProgram not found with id : '99'");

        verify(repository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("updateTrainingProgram updates entity and returns updated DTO")
    void updateTrainingProgram_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(repository.save(any(TrainingProgram.class))).thenReturn(sampleEntity);

        sampleRequest.setTitle("Advanced Java Bootcamp");
        sampleRequest.setStatus("COMPLETED");

        TrainingProgramResponse updated = service.updateTrainingProgram(1L, sampleRequest);

        assertThat(updated).isNotNull();
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(sampleEntity);
    }

    @Test
    @DisplayName("updateTrainingProgram throws ResourceNotFoundException when id does not exist")
    void updateTrainingProgram_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateTrainingProgram(99L, sampleRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, times(1)).findById(99L);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deleteTrainingProgram deletes when id exists")
    void deleteTrainingProgram_Success() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.deleteTrainingProgram(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteTrainingProgram throws ResourceNotFoundException when id does not exist")
    void deleteTrainingProgram_NotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteTrainingProgram(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(any());
    }
}
