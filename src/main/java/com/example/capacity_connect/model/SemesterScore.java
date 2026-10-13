package com.example.capacity_connect.model;

import jakarta.persistence.*;

@Entity
@Table(name = "semester_scores")
public class SemesterScore {

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

    @Column(nullable = false, length = 50)
    private String semester; // e.g. "Semester 5", "Semester 6"

    private Double internal1; // Max 20

    private Double internal2; // Max 20

    private Double assignmentScore; // Max 10

    private Double endSemScore; // Max 50

    @Column(nullable = false)
    private Double totalMarks; // Max 100

    @Column(nullable = false, length = 10)
    private String grade; // O, A+, A, B+, B, C, F

    @Column(nullable = false)
    private Integer credits;

    private Double gradePoints; // 10.0, 9.0, 8.0 etc.

    @Column(nullable = false, length = 50)
    private String status; // PASSED, ARREAR, IN_PROGRESS

    public SemesterScore() {
    }

    public SemesterScore(Long id, Long userId, String studentName, Long courseId, String courseTitle,
                         String semester, Double internal1, Double internal2, Double assignmentScore,
                         Double endSemScore, Integer credits) {
        this.id = id;
        this.userId = userId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.semester = semester;
        this.internal1 = internal1 != null ? internal1 : 0.0;
        this.internal2 = internal2 != null ? internal2 : 0.0;
        this.assignmentScore = assignmentScore != null ? assignmentScore : 0.0;
        this.endSemScore = endSemScore != null ? endSemScore : 0.0;
        this.credits = credits != null ? credits : 4;
        recalculateGrade();
    }

    public void recalculateGrade() {
        double i1 = internal1 != null ? internal1 : 0.0;
        double i2 = internal2 != null ? internal2 : 0.0;
        double asg = assignmentScore != null ? assignmentScore : 0.0;
        double es = endSemScore != null ? endSemScore : 0.0;

        this.totalMarks = Math.round((i1 + i2 + asg + es) * 10.0) / 10.0;

        if (this.totalMarks >= 90.0) {
            this.grade = "O";
            this.gradePoints = 10.0;
            this.status = "PASSED";
        } else if (this.totalMarks >= 80.0) {
            this.grade = "A+";
            this.gradePoints = 9.0;
            this.status = "PASSED";
        } else if (this.totalMarks >= 70.0) {
            this.grade = "A";
            this.gradePoints = 8.0;
            this.status = "PASSED";
        } else if (this.totalMarks >= 60.0) {
            this.grade = "B+";
            this.gradePoints = 7.0;
            this.status = "PASSED";
        } else if (this.totalMarks >= 50.0) {
            this.grade = "B";
            this.gradePoints = 6.0;
            this.status = "PASSED";
        } else if (this.totalMarks >= 40.0) {
            this.grade = "C";
            this.gradePoints = 5.0;
            this.status = "PASSED";
        } else {
            this.grade = "F";
            this.gradePoints = 0.0;
            this.status = "ARREAR";
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Double getInternal1() {
        return internal1;
    }

    public void setInternal1(Double internal1) {
        this.internal1 = internal1;
        recalculateGrade();
    }

    public Double getInternal2() {
        return internal2;
    }

    public void setInternal2(Double internal2) {
        this.internal2 = internal2;
        recalculateGrade();
    }

    public Double getAssignmentScore() {
        return assignmentScore;
    }

    public void setAssignmentScore(Double assignmentScore) {
        this.assignmentScore = assignmentScore;
        recalculateGrade();
    }

    public Double getEndSemScore() {
        return endSemScore;
    }

    public void setEndSemScore(Double endSemScore) {
        this.endSemScore = endSemScore;
        recalculateGrade();
    }

    public Double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Double getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(Double gradePoints) {
        this.gradePoints = gradePoints;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
