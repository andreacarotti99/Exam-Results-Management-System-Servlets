package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.RegisteredStudent;

public class RegisteredStudentsDAO {
	private Connection connection;

	public RegisteredStudentsDAO(Connection connection) {
		this.connection = connection;
	}

	
	public List<RegisteredStudent> findRegisteredStudentsToRound(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, roundId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					
					RegisteredStudent registeredStudent = new RegisteredStudent();
					registeredStudent.setStudentNumber(result.getInt("s.studentnumber"));
					registeredStudent.setSurname(result.getString("u.surname"));
					registeredStudent.setName(result.getString("u.name"));
					registeredStudent.setMail(result.getString("u.email"));
					registeredStudent.setDegreeCourse(result.getString("s.degreecourse"));
					registeredStudent.setMark(result.getInt("r.mark"));
					registeredStudent.setStatus(result.getInt("r.state"));
					registeredStudent.setId(result.getInt("r.idstudent"));
					registeredStudents.add(registeredStudent);

				}
			}
		return registeredStudents;	
		}
	}
	
	
	//it is supposed to extract a list of only one element
	public List<RegisteredStudent> findInfoStudentByRoundIDAndStudentID(int professorId, int roundId, int studentId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where r.idround = ? and r.idstudent = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, roundId);
			pstatement.setInt(2, studentId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					
					RegisteredStudent registeredStudent = new RegisteredStudent();
					registeredStudent.setId(result.getInt("r.idstudent"));
					registeredStudent.setStudentNumber(result.getInt("s.studentnumber"));
					registeredStudent.setSurname(result.getString("u.surname"));
					registeredStudent.setName(result.getString("u.name"));
					registeredStudent.setMail(result.getString("u.email"));
					registeredStudent.setDegreeCourse(result.getString("s.degreecourse"));
					registeredStudent.setMark(result.getInt("r.mark"));
					registeredStudent.setStatus(result.getInt("r.state"));
					registeredStudents.add(registeredStudent);

				}
			}
		return registeredStudents;	
		}
	}
	

	
	
	
}