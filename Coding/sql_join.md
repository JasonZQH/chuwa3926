# SQL JOIN

A join in RDBMS is used to combine rows from two or more tables based on a related column.
It allows us to retrieve related data stored in separate tables.

For example, if a student table stores dept_id, and a department table stores department information, we can use a join to get both student names and department names in one query.

Example tables
	•	student(student_id, student_name, dept_id)
	•	department(dept_id, dept_name)

1. INNER JOIN
```sql
SELECT s.student_name, d.dept_name
FROM student s
INNER JOIN department d
ON s.dept_id = d.dept_id;
```

2. LEFT JOIN
```sql
SELECT s.student_name, d.dept_name
FROM student s
LEFT JOIN department d
ON s.dept_id = d.dept_id;
```

3. RIGHT JOIN
```sql
SELECT s.student_name, d.dept_name
FROM student s
RIGHT JOIN department d
ON s.dept_id = d.dept_id;
```

4. FULL OUTER JOIN
```sql
SELECT s.student_name, d.dept_name
FROM student s
FULL OUTER JOIN department d
ON s.dept_id = d.dept_id;
```

5. Cartesian Product
```sql
SELECT s.student_name, d.dept_name
FROM student s, department d;
```