package Coding;

import java.util.ArrayList;
import java.util.List;

public class Q17_UniversitySystem {
    static class Professor {
        private String name;
        private String specialization;

        public Professor(String name, String specialization) {
            this.name = name;
            this.specialization = specialization;
        }

        @Override
        public String toString() {
            return "Professor{name='" + name + "', specialization='" + specialization + "'}";
        }
    }

    static class Department {
        private String name;
        private String building;

        public Department(String name, String building) {
            this.name = name;
            this.building = building;
        }

        @Override
        public String toString() {
            return "Department{name='" + name + "', building='" + building + "'}";
        }
    }

    static class University {
        private String name;
        private List<Department> departments;
        private List<Professor> professors;

        public University(String name) {
            this.name = name;
            this.departments = new ArrayList<>();
            this.professors = new ArrayList<>();

            // Composition: departments are created internally
            departments.add(new Department("Computer Science", "Engineering Building"));
            departments.add(new Department("Mathematics", "Science Building"));
            departments.add(new Department("Physics", "Research Building"));
        }

        public void addProfessor(Professor p) {
            professors.add(p);
        }

        public void listProfessors() {
            System.out.println("Professors in " + name + ":");
            for (Professor professor : professors) {
                System.out.println(professor);
            }
        }

        public void listDepartments() {
            System.out.println("Departments in " + name + ":");
            for (Department department : departments) {
                System.out.println(department);
            }
        }
    }

    public static void main(String[] args) {
        // Aggregation: professors are created independently
        Professor p1 = new Professor("Alice", "Computer Science");
        Professor p2 = new Professor("Bob", "Mathematics");

        University university = new University("Chuwa University");

        university.addProfessor(p1);
        university.addProfessor(p2);

        university.listDepartments();
        System.out.println();
        university.listProfessors();

        // Demonstrate professors still exist after university is set to null
        university = null;

        System.out.println("\nAfter setting university to null:");
        System.out.println(p1);
        System.out.println(p2);
    }
}
