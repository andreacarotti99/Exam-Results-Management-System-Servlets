package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.RegisteredStudent;

public class OrderDAO {
	private Connection connection;

	public OrderDAO(Connection connection) {
		this.connection = connection;
	}

	
	public List<RegisteredStudent> orderByStudIdAsc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by r.idstudent asc ";
		
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
	
	public List<RegisteredStudent> orderByStudIdDesc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by r.idstudent desc ";
		
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
	
	public List<RegisteredStudent> orderByMarkAsc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by r.mark asc ";
		
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
	
	public List<RegisteredStudent> orderByMarkDesc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by r.mark desc ";
		
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

	public List<RegisteredStudent> orderByRoundIdAsc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by r.idround asc ";
		
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
	
	public List<RegisteredStudent> orderByRoundIdDesc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by r.idround desc ";
		
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
	
	public List<RegisteredStudent> orderByNameAsc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by u.name asc ";
		
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
	
	public List<RegisteredStudent> orderByNameDesc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by u.name desc ";
		
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
	
	public List<RegisteredStudent> orderBySurnameAsc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by u.surname asc ";
		
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
	
	public List<RegisteredStudent> orderBySurnameDesc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by u.surname desc ";
		
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
	
	public List<RegisteredStudent> orderByMailAsc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by u.email asc ";
		
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
	
	public List<RegisteredStudent> orderByMailDesc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by u.email desc ";
		
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
	
	public List<RegisteredStudent> orderByStudNumbAsc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by s.studentnumber asc ";
		
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
	
	public List<RegisteredStudent> orderByStudNumbDesc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by s.studentnumber desc ";
		
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
	
	public List<RegisteredStudent> orderByDegreeAsc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by s.degreecourse asc ";
		
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
	
	public List<RegisteredStudent> orderByDegreeDesc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by s.degreecourse desc ";
		
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
	
	public List<RegisteredStudent> orderByStatusAsc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by r.state asc ";
		
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
	
	public List<RegisteredStudent> orderByStatusDesc(int professorId, int roundId) throws SQLException {
		List<RegisteredStudent> registeredStudents = new ArrayList<RegisteredStudent>();
		
		String query = "select r.idstudent, r.mark, r.idround, r.state, u.name, u.surname, u.email, s.studentnumber, s.degreecourse from (registered r left join user u on r.idstudent = u.id) join studentinfo s on s.id = u.id where idround = ? order by r.state desc ";
		
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
	
	
	
}