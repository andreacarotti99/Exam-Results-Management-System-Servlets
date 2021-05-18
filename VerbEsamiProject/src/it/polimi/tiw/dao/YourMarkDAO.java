package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.RegisteredStudent;

public class YourMarkDAO {
	private Connection connection;

	public YourMarkDAO(Connection connection) {
		this.connection = connection;
	}

	
	
	//it is supposed to extract a list of only one element
	//the query is the same of findInfoStudent in registeredStudentsDAO
	public List<RegisteredStudent> findInfoMarkStudentByRoundIDAndStudentID(int roundId, int studentId) throws SQLException {
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
					registeredStudent.setRoundID(result.getInt("r.idround"));
					registeredStudents.add(registeredStudent);
					
				}
			}
		return registeredStudents;	
		}
	}
	
	
	public void changeStatusToRifiutato(Integer idstudent, Integer idround) throws SQLException {		

		String query = "UPDATE registered SET state = 3 where idstudent = ? and idround = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {

			pstatement.setInt(1, idstudent);
			pstatement.setInt(2, idround);
			
			pstatement.executeUpdate();
		}
	}
	
	
	
	
	
}