# Exam Results Management System

## Overview

This system streamlines the process of managing exam results within educational institutions, enabling faculty to record, modify, and publish exam grades efficiently, while providing students with access to and control over their results.

## Faculty Portal

### Authentication and Course Navigation

- **Login**: Faculty members authenticate to access their dashboard.
- **Course Selection**: Courses are listed in descending alphabetical order for selection.
- **Exam Session Selection**: Faculty select an exam session date from a list ordered in descending chronological order.

### Managing Exam Results

- **Enrolled Students Overview**: Displays a table of students enrolled in the selected session, including details like student ID, name, email, degree program, grade, and status.
- **Sorting Functionality**: Table columns (e.g., last name, first name, grade) are clickable for sorting in ascending or descending order.

### Grade Management

- **Grade Entry/Modification**: An "EDIT" button next to each student's record allows for grade entry or modification. The system includes categories like absent, postponed, re-examined, and numeric grades from 18 to 30, including '30 with honors'.
- **Publishing Grades**: The "PUBLISH" button changes the grade status from "entered" to "published", making them visible to students.
- **Record Finalization**: The "RECORD" button changes the status from "published" to "recorded" and generates an official exam record.

## Student Portal

### Authentication and Course Navigation

- **Login**: Students log in to access their exam information.
- **Course and Exam Session Selection**: Students select their course and specific exam session date in a manner similar to faculty.

### Exam Results Access

- **Grade Viewing**: The RESULT page shows the grade along with course and session details if published; otherwise, it displays "Grade not yet defined."
- **Grade Rejection Option**: For grades between 18 and 30 or '30 with honors', a "REJECT" button allows students to refuse the grade, updating its status to "rejected" in the faculty's overview.

## System Workflow

- **Initial State**: Grades begin in the "not entered" status.
- **Grade Entry and Updates**: Entering or modifying a grade changes its status to "entered".
- **Grade Publication**: Publishing grades makes them visible to students and updates their status to "published".
- **Grade Rejection**: Students can reject their grade, which then changes its status to "rejected".
- **Final Record**: Recording the grades generates an official record and changes their status to "recorded".

This workflow ensures efficient management of exam results, enhancing transparency and communication between faculty and students.
