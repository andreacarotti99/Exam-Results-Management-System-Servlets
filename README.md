# Exam Results Management System

This system streamlines the process of managing exam results within educational institutions, enabling faculty to record, modify, and publish exam grades efficiently, while providing students with access to and control over their results.

## Features and high-level functionalities

### Faculty Portal

#### Authentication and Course Navigation

- **Login**: Faculty members authenticate to access their dashboard.
- **Course Selection**: Courses are listed in descending alphabetical order for selection.
- **Exam Session Selection**: Faculty select an exam session date from a list ordered in descending chronological order.

#### Managing Exam Results

- **Enrolled Students Overview**: Displays a table of students enrolled in the selected session, including details like student ID, name, email, degree program, grade, and status.
- **Sorting Functionality**: Table columns (e.g., last name, first name, grade) are clickable for sorting in ascending or descending order.

#### Grade Management

- **Grade Entry/Modification**: An "EDIT" button next to each student's record allows for grade entry or modification. The system includes categories like absent, postponed, re-examined, and numeric grades from 18 to 30, including '30 with honors'.
- **Publishing Grades**: The "PUBLISH" button changes the grade status from "entered" to "published", making them visible to students.
- **Record Finalization**: The "RECORD" button changes the status from "published" to "recorded" and generates an official exam record.

### Student Portal

#### Authentication and Course Navigation

- **Login**: Students log in to access their exam information.
- **Course and Exam Session Selection**: Students select their course and specific exam session date in a manner similar to faculty.

#### Exam Results Access

- **Grade Viewing**: The RESULT page shows the grade along with course and session details if published; otherwise, it displays "Grade not yet defined."
- **Grade Rejection Option**: For grades between 18 and 30 or '30 with honors', a "REJECT" button allows students to refuse the grade, updating its status to "rejected" in the faculty's overview.

### System Workflow

- **Initial State**: Grades begin in the "not entered" status.
- **Grade Entry and Updates**: Entering or modifying a grade changes its status to "entered".
- **Grade Publication**: Publishing grades makes them visible to students and updates their status to "published".
- **Grade Rejection**: Students can reject their grade, which then changes its status to "rejected".
- **Final Record**: Recording the grades generates an official record and changes their status to "recorded".

This workflow ensures efficient management of exam results, enhancing transparency and communication between faculty and students.


## System Architecture

### Packages and Classes

- `it.polimi.tiw.beans`: Contains the JavaBeans used to encapsulate the various types of data manipulated by the application.
  - `MarkConversion`: Converts numeric marks to their string equivalents and vice versa.
  - `MarkStatus`: Enumerates the possible states of an exam mark (not inserted, inserted, published, rejected, recorded).
  - `SavedOrder`: Tracks and manages the order of data displayed based on user interactions.
  - `Classe`: Represents the details of an academic class or course, including its identifier and name.
  - `RegisteredStudent`: Holds information about students registered for an exam, including personal details, course, and exam status.
  - `Round`: Details an exam session or round, including its date and associated class.
  - `User`: Represents a user of the system, storing personal details, role, and authentication information.
  - `Verbal`: Records the details of an exam verbalization process, including its identifier and timestamp.


- `it.polimi.tiw.controllers`: Manages the application's workflow by handling HTTP requests and responses.
  - Servlets like `GoToClassesProfessorListPage` and `GoToRegisteredToRoundPage` direct the flow of the application and render the appropriate views using Thymeleaf templates.

- `it.polimi.tiw.dao`: Responsible for data access operations, providing a layer of abstraction between the application logic and the database.
  - Classes like `GeneralChecksDAO`, `VerbalizationDAO`, and `EditMarkDAO` encapsulate the SQL queries and transactions.

- `it.polimi.tiw.filters`: Implements Servlet Filters to enforce security and session management.
  - `NoCache`: Prevents browser caching to secure sensitive data.
  - `ProfessorFilter` and `StudentFilter`: Ensure that users access only the functionalities available to their roles.
  - `CheckSessionLogin`: Checks whether the user session is valid to prevent unauthorized access.

- `it.polimi.tiw.utils`: Contains utility classes.
  - `ConnectionHandler`: Manages the database connection lifecycle.

### Security and Data Integrity

- **Role-Based Access Control (RBAC)**: The application implements RBAC through filters (`ProfessorFilter`, `StudentFilter`) ensuring users can only access functionalities pertinent to their roles.
- **Session Management**: The system validates sessions using `CheckSessionLogin` to prevent unauthorized access.
- **Data Validation**: Controllers validate incoming data to prevent SQL injection and other common web vulnerabilities.
- **Transaction Management**: DAO classes manage database transactions to ensure data consistency and handle transactional errors gracefully.

### Infrastructure

The application is designed to be deployed on a servlet container (e.g., Apache Tomcat) and uses a MySQL database for persistence. It utilizes the Thymeleaf templating engine for dynamic web page rendering and the JDBC API for database connectivity.

### Setup and Deployment

1. **Database Configuration**: Set up a MySQL database and update the `dbUrl`, `dbUser`, and `dbPassword` parameters in the web.xml file to match your database credentials.
2. **Server Deployment**: Deploy the application on a servlet container like Apache Tomcat by packaging it into a WAR file and placing it in the server's webapps directory.
3. **Initialization**: On the first run, the application establishes a connection to the database and initializes the data model.

## Conclusion

This system is designed with a focus on security, scalability, and maintainability, utilizing established Java EE technologies and patterns. The architecture facilitates future extensions and integrations, ensuring the application can evolve to meet changing requirements.

