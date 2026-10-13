package com.example.capacity_connect.controller;

import com.example.capacity_connect.model.AttendanceRecord;
import com.example.capacity_connect.model.TrainingProgram;
import com.example.capacity_connect.model.User;
import com.example.capacity_connect.repository.AttendanceRepository;
import com.example.capacity_connect.repository.TrainingProgramRepository;
import com.example.capacity_connect.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final TrainingProgramRepository programRepository;

    public AttendanceController(AttendanceRepository attendanceRepository,
                                UserRepository userRepository,
                                TrainingProgramRepository programRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.programRepository = programRepository;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceRecord>> getAllAttendance() {
        return ResponseEntity.ok(attendanceRepository.findAll());
    }

    @GetMapping("/student/{userId}")
    public ResponseEntity<List<AttendanceRecord>> getStudentAttendance(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceRepository.findByUserId(userId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AttendanceRecord>> getCourseAttendance(@PathVariable Long courseId) {
        return ResponseEntity.ok(attendanceRepository.findByCourseId(courseId));
    }

    @PostMapping
    public ResponseEntity<AttendanceRecord> createAttendanceRecord(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long courseId = Long.valueOf(payload.get("courseId").toString());
        Integer totalClasses = payload.containsKey("totalClasses") ? Integer.valueOf(payload.get("totalClasses").toString()) : 30;
        Integer attendedClasses = payload.containsKey("attendedClasses") ? Integer.valueOf(payload.get("attendedClasses").toString()) : 25;
        String remarks = payload.containsKey("remarks") ? payload.get("remarks").toString() : "Semester Session";

        String studentName = userRepository.findById(userId).map(User::getName).orElse("Student " + userId);
        String courseTitle = programRepository.findById(courseId).map(TrainingProgram::getTitle).orElse("Course " + courseId);

        AttendanceRecord record = attendanceRepository.findByUserIdAndCourseId(userId, courseId)
                .orElse(new AttendanceRecord(null, userId, studentName, courseId, courseTitle, totalClasses, attendedClasses, remarks));

        record.setTotalClasses(totalClasses);
        record.setAttendedClasses(attendedClasses);
        record.setRemarks(remarks);
        record.setLastUpdatedDate(LocalDate.now());
        record.recalculateStatus();

        AttendanceRecord saved = attendanceRepository.save(record);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceRecord> updateAttendance(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        return attendanceRepository.findById(id).map(record -> {
            if (payload.containsKey("totalClasses")) {
                record.setTotalClasses(Integer.valueOf(payload.get("totalClasses").toString()));
            }
            if (payload.containsKey("attendedClasses")) {
                record.setAttendedClasses(Integer.valueOf(payload.get("attendedClasses").toString()));
            }
            if (payload.containsKey("remarks")) {
                record.setRemarks(payload.get("remarks").toString());
            }
            record.setLastUpdatedDate(LocalDate.now());
            record.recalculateStatus();
            return ResponseEntity.ok(attendanceRepository.save(record));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/mark")
    public ResponseEntity<AttendanceRecord> markAttendance(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        boolean isPresent = payload.containsKey("present") && Boolean.parseBoolean(payload.get("present").toString());
        return attendanceRepository.findById(id).map(record -> {
            record.setTotalClasses(record.getTotalClasses() + 1);
            if (isPresent) {
                record.setAttendedClasses(record.getAttendedClasses() + 1);
            }
            record.setLastUpdatedDate(LocalDate.now());
            record.recalculateStatus();
            return ResponseEntity.ok(attendanceRepository.save(record));
        }).orElse(ResponseEntity.notFound().build());
    }
}
