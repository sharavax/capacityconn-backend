package com.example.capacity_connect.config;

import com.example.capacity_connect.model.*;
import com.example.capacity_connect.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TrainingProgramRepository programRepository;
    private final CompetencyRepository competencyRepository;
    private final EmployeeCompetencyRepository employeeCompetencyRepository;
    private final LearningResourceRepository resourceRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssessmentRepository assessmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final SemesterScoreRepository semesterScoreRepository;

    public DataInitializer(UserRepository userRepository,
                           TrainingProgramRepository programRepository,
                           CompetencyRepository competencyRepository,
                           EmployeeCompetencyRepository employeeCompetencyRepository,
                           LearningResourceRepository resourceRepository,
                           EnrollmentRepository enrollmentRepository,
                           AssessmentRepository assessmentRepository,
                           AttendanceRepository attendanceRepository,
                           SemesterScoreRepository semesterScoreRepository) {
        this.userRepository = userRepository;
        this.programRepository = programRepository;
        this.competencyRepository = competencyRepository;
        this.employeeCompetencyRepository = employeeCompetencyRepository;
        this.resourceRepository = resourceRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.assessmentRepository = assessmentRepository;
        this.attendanceRepository = attendanceRepository;
        this.semesterScoreRepository = semesterScoreRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedTrainingPrograms();
        seedCompetencies();
        seedLearningResources();
        seedEnrollmentsAndAssessments();
        seedAttendance();
        seedSemesterScores();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            userRepository.saveAll(List.of(
                    new User(null, "Dr. Ananya Rao", "ananya.rao@capacity.edu", "ADMIN", "B.Tech Academic Planning", "Program Coordinator"),
                    new User(null, "Prof. Karthik Menon", "karthik.menon@capacity.edu", "TRAINER", "Computer Science & Engineering", "Faculty Mentor"),
                    new User(null, "Meera Nair", "meera.nair@capacity.edu", "EMPLOYEE", "Semester 6", "B.Tech CSE Student"),
                    new User(null, "Arjun Iyer", "arjun.iyer@capacity.edu", "EMPLOYEE", "Semester 4", "B.Tech CSE Student"),
                    new User(null, "Nisha Kapoor", "nisha.kapoor@capacity.edu", "EMPLOYEE", "Semester 8", "B.Tech CSE Student")
            ));
            return;
        }

        replaceLegacyUser("rajesh.sharma@imd.gov.in",
                new User(null, "Dr. Ananya Rao", "ananya.rao@capacity.edu", "ADMIN", "B.Tech Academic Planning", "Program Coordinator"));
        replaceLegacyUser("sunita.sen@imd.gov.in",
                new User(null, "Prof. Karthik Menon", "karthik.menon@capacity.edu", "TRAINER", "Computer Science & Engineering", "Faculty Mentor"));
        replaceLegacyUser("amit.verma@imd.gov.in",
                new User(null, "Meera Nair", "meera.nair@capacity.edu", "EMPLOYEE", "Semester 6", "B.Tech CSE Student"));
        replaceLegacyUser("priya.patel@imd.gov.in",
                new User(null, "Arjun Iyer", "arjun.iyer@capacity.edu", "EMPLOYEE", "Semester 4", "B.Tech CSE Student"));
        replaceLegacyUser("vikram.m@imd.gov.in",
                new User(null, "Nisha Kapoor", "nisha.kapoor@capacity.edu", "EMPLOYEE", "Semester 8", "B.Tech CSE Student"));
    }

    private void seedTrainingPrograms() {
        if (programRepository.count() == 0) {
            programRepository.saveAll(defaultPrograms());
            return;
        }

        replaceLegacyProgram("Doppler Weather Radar (DWR) Operations & Maintenance", defaultPrograms().get(0));
        replaceLegacyProgram("High-Resolution Numerical Weather Prediction (WRF Modeling)", defaultPrograms().get(1));
        replaceLegacyProgram("Tropical Cyclone Track & Intensity Forecasting", defaultPrograms().get(2));
        replaceLegacyProgram("AI & Deep Learning for Precipitation Nowcasting", defaultPrograms().get(3));
        replaceLegacyProgram("Aviation Weather Services & SIGMET Issuance", defaultPrograms().get(4));
    }

    private void seedCompetencies() {
        List<Competency> comps;
        if (competencyRepository.count() == 0) {
            comps = competencyRepository.saveAll(defaultCompetencies());
        } else {
            replaceLegacyCompetency("Doppler Radar Calibration & Echo Interpretation", defaultCompetencies().get(0));
            replaceLegacyCompetency("Numerical Weather Modeling (WRF/GFS)", defaultCompetencies().get(1));
            replaceLegacyCompetency("Satellite Meteorology & Dvorak Technique", defaultCompetencies().get(2));
            replaceLegacyCompetency("Python for Climate & Gridded Data (NetCDF/GRIB)", defaultCompetencies().get(3));
            replaceLegacyCompetency("Severe Weather Early Warning Dissemination", defaultCompetencies().get(4));
            comps = competencyRepository.findAll();
        }

        User student = userRepository.findAll().stream()
                .filter(u -> "EMPLOYEE".equals(u.getRole()))
                .findFirst().orElse(null);

        if (student != null && employeeCompetencyRepository.count() == 0 && comps.size() >= 5) {
            employeeCompetencyRepository.saveAll(List.of(
                    new EmployeeCompetency(null, student.getId(), comps.get(0).getId(), 3, 5, LocalDate.of(2026, 10, 1)),
                    new EmployeeCompetency(null, student.getId(), comps.get(1).getId(), 2, 4, LocalDate.of(2026, 10, 1)),
                    new EmployeeCompetency(null, student.getId(), comps.get(2).getId(), 3, 4, LocalDate.of(2026, 10, 1)),
                    new EmployeeCompetency(null, student.getId(), comps.get(3).getId(), 2, 4, LocalDate.of(2026, 10, 1)),
                    new EmployeeCompetency(null, student.getId(), comps.get(4).getId(), 4, 4, LocalDate.of(2026, 10, 1))
            ));
        }
    }

    private void seedLearningResources() {
        if (resourceRepository.count() == 0) {
            resourceRepository.saveAll(defaultResources());
            return;
        }

        replaceLegacyResource("IMD Doppler Weather Radar Standard Operating Manual", defaultResources().get(0));
        replaceLegacyResource("WRF-ARW Model User Guide & Practical Lab Exercises", defaultResources().get(1));
        replaceLegacyResource("Tropical Cyclone Early Warning Guidelines (SOP-TC24)", defaultResources().get(2));
        replaceLegacyResource("INSAT-3D Multispectral Imagery Interpretation Lecture Video", defaultResources().get(3));
        replaceLegacyResource("Climate Analytics with Python (Jupyter Notebooks Repository)", defaultResources().get(4));
    }

    private void seedEnrollmentsAndAssessments() {
        User student = userRepository.findAll().stream()
                .filter(u -> "EMPLOYEE".equals(u.getRole()))
                .findFirst().orElse(null);

        if (enrollmentRepository.count() == 0 && student != null) {
            enrollmentRepository.saveAll(List.of(
                    new Enrollment(null, student.getId(), student.getName(), 1L, "Data Structures and Algorithms - Semester 3", LocalDate.of(2026, 10, 2), 65, "IN_PROGRESS", 82.0),
                    new Enrollment(null, student.getId(), student.getName(), 3L, "Operating Systems - Semester 5", LocalDate.of(2026, 10, 5), 35, "IN_PROGRESS", null),
                    new Enrollment(null, student.getId(), student.getName(), 5L, "Software Engineering & Agile Project Lab - Semester 7", LocalDate.of(2026, 9, 1), 100, "COMPLETED", 94.0)
            ));
        } else {
            replaceLegacyEnrollmentTitle("Doppler Weather Radar (DWR) Operations & Maintenance", "Data Structures and Algorithms - Semester 3");
            replaceLegacyEnrollmentTitle("Tropical Cyclone Track & Intensity Forecasting", "Operating Systems - Semester 5");
            replaceLegacyEnrollmentTitle("Aviation Weather Services & SIGMET Issuance", "Software Engineering & Agile Project Lab - Semester 7");
        }

        if (assessmentRepository.count() == 0) {
            assessmentRepository.save(defaultAssessment());
        } else {
            replaceLegacyAssessment("Doppler Weather Radar (DWR) Operations & Maintenance", defaultAssessment());
        }
    }

    private List<TrainingProgram> defaultPrograms() {
        return List.of(
                new TrainingProgram(
                        null,
                        "Data Structures and Algorithms - Semester 3",
                        "Arrays, stacks, queues, linked lists, trees, graphs, hashing, sorting, complexity analysis, and lab-based problem solving.",
                        "16 weeks",
                        LocalDate.of(2026, 10, 20),
                        LocalDate.of(2027, 2, 10),
                        "ACTIVE"
                ),
                new TrainingProgram(
                        null,
                        "Database Management Systems - Semester 4",
                        "ER modeling, relational algebra, SQL, normalization, transactions, indexing, and practical schema design for mini projects.",
                        "14 weeks",
                        LocalDate.of(2026, 11, 15),
                        LocalDate.of(2027, 2, 25),
                        "UPCOMING"
                ),
                new TrainingProgram(
                        null,
                        "Operating Systems - Semester 5",
                        "Processes, threads, scheduling, synchronization, deadlocks, memory management, file systems, and shell scripting labs.",
                        "15 weeks",
                        LocalDate.of(2026, 10, 1),
                        LocalDate.of(2027, 1, 20),
                        "ACTIVE"
                ),
                new TrainingProgram(
                        null,
                        "Machine Learning Foundations - Semester 6",
                        "Supervised learning, regression, classification, model evaluation, feature engineering, and notebook-based experiments.",
                        "12 weeks",
                        LocalDate.of(2026, 12, 1),
                        LocalDate.of(2027, 2, 28),
                        "UPCOMING"
                ),
                new TrainingProgram(
                        null,
                        "Software Engineering & Agile Project Lab - Semester 7",
                        "Requirements, UML, design patterns, testing, sprint planning, Git workflows, and project documentation rubrics.",
                        "10 weeks",
                        LocalDate.of(2026, 8, 1),
                        LocalDate.of(2026, 10, 15),
                        "COMPLETED"
                )
        );
    }

    private List<Competency> defaultCompetencies() {
        return List.of(
                new Competency(null, "Algorithmic Problem Solving", "Data Structures", "Analyze complexity, choose suitable data structures, and solve lab/coding problems with clean implementations.", 5, "Computer Science & Engineering"),
                new Competency(null, "Relational Schema Design & SQL", "Database Systems", "Model entities, normalize schemas, write SQL queries, and explain transaction behavior.", 4, "Computer Science & Engineering"),
                new Competency(null, "OS Process, Memory & File System Concepts", "Operating Systems", "Explain scheduling, synchronization, paging, deadlocks, and implement basic shell/file utilities.", 4, "Computer Science & Engineering"),
                new Competency(null, "ML Model Evaluation & Experimentation", "Data Science & AI", "Build baseline models, evaluate metrics, tune features, and document notebook experiments.", 4, "Computer Science & Engineering"),
                new Competency(null, "Software Design, Testing & Documentation", "Software Engineering", "Translate requirements into design, write tests, manage Git workflow, and maintain project documentation.", 4, "Computer Science & Engineering")
        );
    }

    private List<LearningResource> defaultResources() {
        return List.of(
                new LearningResource(
                        null,
                        "DSA Unit Notes: Trees, Graphs, and Complexity",
                        "Data Structures",
                        "NOTES",
                        "Concise semester notes covering traversal, shortest paths, hashing, sorting, and asymptotic analysis.",
                        "https://library.capacity.edu/btech/dsa-unit-notes.pdf",
                        "DSA, Trees, Graphs, Complexity",
                        1L
                ),
                new LearningResource(
                        null,
                        "DBMS Lab Manual: SQL, Normalization, Transactions",
                        "Database Systems",
                        "LAB_MANUAL",
                        "Lab exercises for schema design, joins, subqueries, normalization, ACID transactions, and indexing practice.",
                        "https://library.capacity.edu/btech/dbms-lab-manual.pdf",
                        "DBMS, SQL, Normalization, Transactions",
                        2L
                ),
                new LearningResource(
                        null,
                        "Operating Systems Viva Question Bank",
                        "Operating Systems",
                        "QUESTION_BANK",
                        "Short-answer prompts for process scheduling, synchronization, memory allocation, deadlocks, and file systems.",
                        "https://library.capacity.edu/btech/os-viva-bank.pdf",
                        "OS, Viva, Scheduling, Memory",
                        3L
                ),
                new LearningResource(
                        null,
                        "Machine Learning Lecture Series: Regression to Model Evaluation",
                        "Data Science & AI",
                        "VIDEO",
                        "Recorded lectures with notebook demos for regression, classification, cross-validation, confusion matrices, and model selection.",
                        "https://learning.capacity.edu/videos/ml-foundations",
                        "ML, Regression, Classification, Metrics",
                        4L
                ),
                new LearningResource(
                        null,
                        "Software Engineering Project Template and Rubric",
                        "Software Engineering",
                        "DOCUMENT",
                        "Project structure, requirement templates, sprint board checklist, test plan format, and evaluation rubric.",
                        "https://library.capacity.edu/btech/se-project-rubric.pdf",
                        "Agile, UML, Testing, Git",
                        5L
                )
        );
    }

    private Assessment defaultAssessment() {
        String dsaQuestions = "[\n" +
                "  {\"question\": \"Which data structure is most suitable for breadth-first traversal of a graph?\", \"options\": [\"Stack\", \"Queue\", \"Priority queue\", \"Hash table\"], \"correctIndex\": 1, \"explanation\": \"Breadth-first traversal explores nodes level by level, which is naturally supported by a queue.\"},\n" +
                "  {\"question\": \"What is the average-case time complexity of searching in a well-designed hash table?\", \"options\": [\"O(1)\", \"O(log n)\", \"O(n)\", \"O(n log n)\"], \"correctIndex\": 0, \"explanation\": \"With a good hash function and controlled load factor, hash-table lookup is O(1) on average.\"},\n" +
                "  {\"question\": \"Which traversal of a binary search tree returns values in sorted order?\", \"options\": [\"Preorder\", \"Inorder\", \"Postorder\", \"Level order\"], \"correctIndex\": 1, \"explanation\": \"Inorder traversal visits left subtree, root, and right subtree, producing sorted values for a BST.\"}\n" +
                "]";

        return new Assessment(
                null,
                1L,
                "Data Structures and Algorithms - Semester 3",
                "DSA Course Outcome Check",
                dsaQuestions,
                70
        );
    }

    private void replaceLegacyUser(String legacyEmail, User replacement) {
        Optional<User> legacy = userRepository.findByEmail(legacyEmail);
        if (legacy.isEmpty()) return;

        User user = legacy.get();
        Optional<User> existingReplacement = userRepository.findByEmail(replacement.getEmail());
        if (existingReplacement.isEmpty() || existingReplacement.get().getId().equals(user.getId())) {
            user.setEmail(replacement.getEmail());
        }
        user.setName(replacement.getName());
        user.setRole(replacement.getRole());
        user.setDepartment(replacement.getDepartment());
        user.setDesignation(replacement.getDesignation());
        userRepository.save(user);
    }

    private void replaceLegacyProgram(String legacyTitle, TrainingProgram replacement) {
        programRepository.findAll().stream()
                .filter(program -> legacyTitle.equals(program.getTitle()))
                .findFirst()
                .ifPresent(program -> {
                    program.setTitle(replacement.getTitle());
                    program.setDescription(replacement.getDescription());
                    program.setDuration(replacement.getDuration());
                    program.setStartDate(replacement.getStartDate());
                    program.setEndDate(replacement.getEndDate());
                    program.setStatus(replacement.getStatus());
                    programRepository.save(program);
                });
    }

    private void replaceLegacyCompetency(String legacyName, Competency replacement) {
        competencyRepository.findAll().stream()
                .filter(competency -> legacyName.equals(competency.getName()))
                .findFirst()
                .ifPresent(competency -> {
                    competency.setName(replacement.getName());
                    competency.setCategory(replacement.getCategory());
                    competency.setDescription(replacement.getDescription());
                    competency.setTargetBenchmarkLevel(replacement.getTargetBenchmarkLevel());
                    competency.setApplicableDepartment(replacement.getApplicableDepartment());
                    competencyRepository.save(competency);
                });
    }

    private void replaceLegacyResource(String legacyTitle, LearningResource replacement) {
        resourceRepository.findAll().stream()
                .filter(resource -> legacyTitle.equals(resource.getTitle()))
                .findFirst()
                .ifPresent(resource -> {
                    resource.setTitle(replacement.getTitle());
                    resource.setCategory(replacement.getCategory());
                    resource.setResourceType(replacement.getResourceType());
                    resource.setDescription(replacement.getDescription());
                    resource.setContentOrUrl(replacement.getContentOrUrl());
                    resource.setTags(replacement.getTags());
                    resource.setProgramId(replacement.getProgramId());
                    resourceRepository.save(resource);
                });
    }

    private void replaceLegacyEnrollmentTitle(String legacyTitle, String replacementTitle) {
        enrollmentRepository.findAll().stream()
                .filter(enrollment -> legacyTitle.equals(enrollment.getProgramTitle()))
                .forEach(enrollment -> {
                    enrollment.setProgramTitle(replacementTitle);
                    enrollmentRepository.save(enrollment);
                });
    }

    private void replaceLegacyAssessment(String legacyProgramTitle, Assessment replacement) {
        assessmentRepository.findAll().stream()
                .filter(assessment -> legacyProgramTitle.equals(assessment.getProgramTitle()))
                .findFirst()
                .ifPresent(assessment -> {
                    assessment.setProgramTitle(replacement.getProgramTitle());
                    assessment.setTitle(replacement.getTitle());
                    assessment.setQuestionsJson(replacement.getQuestionsJson());
                    assessment.setPassingScore(replacement.getPassingScore());
                    assessmentRepository.save(assessment);
                });
    }

    private void seedAttendance() {
        if (attendanceRepository.count() == 0) {
            User student = userRepository.findAll().stream()
                    .filter(u -> "EMPLOYEE".equals(u.getRole()))
                    .findFirst().orElse(null);

            if (student != null) {
                attendanceRepository.saveAll(List.of(
                        new AttendanceRecord(null, student.getId(), student.getName(), 1L,
                                "Data Structures and Algorithms - Semester 3", 45, 41, "Regular laboratory attendance"),
                        new AttendanceRecord(null, student.getId(), student.getName(), 2L,
                                "Database Management Systems - Semester 4", 40, 36, "Good theory and mini-project participation"),
                        new AttendanceRecord(null, student.getId(), student.getName(), 3L,
                                "Operating Systems - Semester 5", 42, 29, "Medical leave in Week 4; shortage warning"),
                        new AttendanceRecord(null, student.getId(), student.getName(), 4L,
                                "Machine Learning Foundations - Semester 6", 36, 34, "Excellent attendance and lab notebooks"),
                        new AttendanceRecord(null, student.getId(), student.getName(), 5L,
                                "Software Engineering & Agile Project Lab - Semester 7", 32, 30, "Active sprint standup attendance")
                ));
            }
        }
    }

    private void seedSemesterScores() {
        if (semesterScoreRepository.count() == 0) {
            User student = userRepository.findAll().stream()
                    .filter(u -> "EMPLOYEE".equals(u.getRole()))
                    .findFirst().orElse(null);

            if (student != null) {
                semesterScoreRepository.saveAll(List.of(
                        new SemesterScore(null, student.getId(), student.getName(), 1L,
                                "Data Structures and Algorithms - Semester 3", "Semester 3",
                                18.0, 18.5, 9.5, 44.0, 4),
                        new SemesterScore(null, student.getId(), student.getName(), 2L,
                                "Database Management Systems - Semester 4", "Semester 4",
                                16.5, 17.0, 8.5, 40.0, 4),
                        new SemesterScore(null, student.getId(), student.getName(), 3L,
                                "Operating Systems - Semester 5", "Semester 5",
                                15.0, 16.0, 8.0, 36.0, 4),
                        new SemesterScore(null, student.getId(), student.getName(), 4L,
                                "Machine Learning Foundations - Semester 6", "Semester 6",
                                17.5, 18.0, 9.0, 42.0, 3),
                        new SemesterScore(null, student.getId(), student.getName(), 5L,
                                "Software Engineering & Agile Project Lab - Semester 7", "Semester 6",
                                18.0, 19.0, 9.5, 45.0, 3)
                ));
            }
        }
    }
}
