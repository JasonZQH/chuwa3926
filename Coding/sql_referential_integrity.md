# SQL Referential Integrity

## Issues in the Original Code
1. `ENUM` cannot be used in PostgreSQL
2. Relation "department" does not exist; the school table should be created before the department table, and department table should be created before the student table.
3. Insert or update on table "student" violates foreign key constraint "student_dept_id_fkey" Key (dept_id)=(101) is not present in table "department". The data insertion order is wrong.
4. Missing data from parent table (school)
5. Duplicate key value violates primary key:
    ```sql
    (1002, 'Ziwei Zhang', 'Female', '2002-08-12', 102),
    (1002, 'Tyler A.', 'Female', '2002-08-12', 666);
    ```
6. Mismatch the column of department:
    ```sql
    INSERT INTO department VALUES (102, 'Physics', 888);
    ```
7. Delete referenced item from department table:
    ```sql
    DELETE FROM department WHERE dept_id = 101;
    ```

## Referential Integrity
Referential integrity ensures that relationships between tables remains consistent. A foreign key in a child table must match an existing primary key in the parent table.
It prevents:
- inserting invalid foreign key values
- deleting parent records that are still referenced
- updating referenced keys incorrectly

Here is corrected example code:
```sql
-- Create School Schema --
CREATE TABLE school (
    school_id INT PRIMARY KEY,
    school_name VARCHAR(100) NOT NULL,
    city VARCHAR(50),
    established_year INT
);

-- Create Department Schema --
CREATE TABLE department (
    dept_id INT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    building VARCHAR(50),
    school_id INT,
    FOREIGN KEY (school_id) REFERENCES school(school_id)
);

-- Create Student Schema --
CREATE TABLE student (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female')) NOT NULL,
    birth_date DATE,
    dept_id INT,
    FOREIGN KEY (dept_id) REFERENCES department(dept_id)
);

-- Insert data to school table --
INSERT INTO school (school_id, school_name, city, established_year)
VALUES
(1, 'Engineering School', 'San Jose', 1990),
(2, 'Law School', 'San Francisco', 1985);

-- Insert data to department table --
INSERT INTO department (dept_id, dept_name, building, school_id)
VALUES 
(101, 'Computer Science', 'Information Hall', 1),
(102, 'Electrical Engineering', 'Main Building A', 1),
(201, 'Law', 'Law School Building', 2);

-- Insert data to student table --
INSERT INTO student (student_id, student_name, gender, birth_date, dept_id)
VALUES 
(1001, 'John Zhang', 'Male', '2001-05-01', 101),
(1002, 'Lisa Li', 'Female', '2002-08-12', 102),
(1003, 'Kevin Wang', 'Male', '2000-11-21', 201);
```




