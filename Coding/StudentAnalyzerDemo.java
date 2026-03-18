package Coding;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class Student {
    private String id;
    private String name;
    private int age;
    private String major;
    private List<Double> scores;

    public Student(String id, String name, int age, String major, List<Double> scores) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.scores = scores;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getMajor() {
        return major;
    }

    public List<Double> getScores() {
        return scores;
    }

    public double getAvgScore() {
        return scores.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }

    public double getHighestScore() {
        return scores.stream()
            .mapToDouble(Double::doubleValue)
            .max()
            .orElse(0.0);
    }

    @Override
    public String toString() {
        return "Student{id='" + id + "', name='" + name + "', major='" + major +
                "', avg=" + String.format("%.2f", getAvgScore()) + "}";
    }
}

class StudentAnalyzer {
    public List<String> getTopStudentNames(List<Student> students, int n) {
        return students.stream()
            .sorted(Comparator.comparingDouble(Student::getAvgScore).reversed())
            .limit(n)
            .map(Student::getName)
            .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageScoreByMajor(List<Student> students) {
        return students.stream()
            .collect(Collectors.groupingBy(
                Student::getMajor,
                Collectors.averagingDouble(Student::getAvgScore)
            ));
    }

    public Optional<Student> findStudentWithHighestSingleScore(List<Student> students) {
        return students.stream()
            .max(Comparator.comparing(Student::getHighestScore));
    }

    public List<Student> getStudentsAboveAverageInMajor(List<Student> students, String major) {
        double majorAvg = students.stream()
            .filter(student -> student.getMajor().equalsIgnoreCase(major))
            .mapToDouble(Student::getAvgScore)
            .average()
            .orElse(0.0);

        return students.stream()
            .filter(student -> student.getMajor().equalsIgnoreCase(major))
            .filter(student -> student.getAvgScore() > majorAvg)
            .collect(Collectors.toList());
    }

    public Map<Boolean, List<Student>> partitionByPassFail(List<Student> students, double passingScore) {
        return students.stream()
            .collect(Collectors.partitioningBy(
                student -> student.getAvgScore() >= passingScore
            ));
    }
}

public class StudentAnalyzerDemo {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("S1", "Alice", 20, "CS", Arrays.asList(95.0, 88.0, 91.0)),
                new Student("S2", "Bob", 21, "CS", Arrays.asList(72.0, 85.0, 78.0)),
                new Student("S3", "Carol", 22, "Math", Arrays.asList(99.0, 92.0, 96.0)),
                new Student("S4", "David", 20, "Math", Arrays.asList(65.0, 70.0, 68.0)),
                new Student("S5", "Eva", 23, "Physics", Arrays.asList(88.0, 84.0, 90.0)),
                new Student("S6", "Frank", 21, "Physics", Arrays.asList(55.0, 60.0, 58.0))
        );

        StudentAnalyzer analyzer = new StudentAnalyzer();

        System.out.println("=== Top 3 Students by Average Score ===");
        List<String> topStudents = analyzer.getTopStudentNames(students, 3);
        topStudents.forEach(System.out::println);

        System.out.println("\n=== Average Score By Major ===");
        Map<String, Double> avgByMajor = analyzer.getAverageScoreByMajor(students);
        avgByMajor.forEach((major, avg) ->
                System.out.println(major + ": " + String.format("%.2f", avg)));

        System.out.println("\n=== Student With Highest Single Score ===");
        Optional<Student> highestSingleScoreStudent = analyzer.findStudentWithHighestSingleScore(students);
        highestSingleScoreStudent.ifPresent(student ->
                System.out.println(student.getName() + " with highest single score: " + student.getHighestScore()));

        System.out.println("\n=== Students Above Average In CS ===");
        List<Student> aboveAverageCS = analyzer.getStudentsAboveAverageInMajor(students, "CS");
        aboveAverageCS.forEach(System.out::println);

        System.out.println("\n=== Partition By Pass/Fail (passing score = 75) ===");
        Map<Boolean, List<Student>> partitioned = analyzer.partitionByPassFail(students, 75.0);

        System.out.println("Passed:");
        partitioned.get(true).forEach(System.out::println);

        System.out.println("Failed:");
        partitioned.get(false).forEach(System.out::println);

        System.out.println("\n=== Optional with orElse ===");
        Student fallbackStudent = highestSingleScoreStudent.orElse(
                new Student("S0", "Default", 0, "None", Arrays.asList(0.0))
        );
        System.out.println("Fallback/Found student: " + fallbackStudent.getName());
    }
}
