package com.example.capacity_connect.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance_records")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String studentName;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false, length = 200)
    private String courseTitle;

    @Column(nullable = false)
    private Integer totalClasses;

    @Column(nullable = false)
    private Integer attendedClasses;

    @Column(nullable = false)
    private Double attendancePercentage;

    @Column(nullable = false, length = 50)
    private String status; // NORMAL, SHORTAGE_WARNING, CRITICAL

    private LocalDate lastUpdatedDate;

    @Column(length = 255)
    private String remarks;

    public AttendanceRecord() {
    }

    public AttendanceRecord(Long id, Long userId, String studentName, Long courseId, String courseTitle,
                            Integer totalClasses, Integer attendedClasses, String remarks) {
        this.id = id;
        this.userId = userId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.totalClasses = totalClasses != null ? totalClasses : 0;
        this.attendedClasses = attendedClasses != null ? attendedClasses : 0;
        this.remarks = remarks;
        this.lastUpdatedDate = LocalDate.now();
        recalculateStatus();
    }

    public void recalculateStatus() {
        if (totalClasses == null || totalClasses == 0) {
            this.attendancePercentage = 100.0;
            this.status = "NORMAL";
        } else {
            double pct = ((double) attendedClasses / totalClasses) * 100.0;
            this.attendancePercentage = Math.round(pct * 10.0) / 10.0;
            if (this.attendancePercentage >= 75.0) {
                this.status = "NORMAL";
            } else if (this.attendancePercentage >= 65.0) {
                this.status = "SHORTAGE_WARNING";
            } else {
                this.status = "CRITICAL";
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Integer getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(Integer totalClasses) {
        this.totalClasses = totalClasses;
        recalculateStatus();
    }

    public Integer getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(Integer attendedClasses) {
        this.attendedClasses = attendedClasses;
        recalculateStatus();
    }

    public Double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(Double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
