package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Round;

public class RoundsDAO {
	private Connection connection;

	public RoundsDAO(Connection connection) {
		this.connection = connection;
	}

	
	public List<Round> findRoundsByProfessorAndClass(int professorId, int classId) throws SQLException {
		List<Round> rounds = new ArrayList<Round>();
		
		String query = "SELECT c.idclass, classname, idprofessor, idround, date from class c inner join round r on (c.idclass = r.idclass) where (c.idclass = ? and  idprofessor = ?) order by date desc";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, classId);
			pstatement.setInt(2, professorId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
				
					Round round = new Round();
					round.setRoundID(result.getInt("idround"));
					round.setDate(result.getDate("date"));
					round.setClassID(result.getInt("idclass"));
					round.setProfessorID(result.getInt("idprofessor"));
					rounds.add(round);
				}
			}
		return rounds;	
		}
	}
	
	
	
	public List<Round> findRoundsByStudentAndClass(int studentId, int classId) throws SQLException {
		List<Round> rounds = new ArrayList<Round>();
		
		String query = "SELECT idstudent, a.idclass, idround, date from attend a join round r on (a.idclass = r.idclass) where idstudent = ? and a.idclass = ? order by date desc";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, studentId);
			pstatement.setInt(2, classId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
				
					Round round = new Round();
					round.setRoundID(result.getInt("idround"));
					round.setDate(result.getDate("date"));
					round.setClassID(result.getInt("idclass"));
					round.setStudentID(result.getInt("idstudent"));
					rounds.add(round);
				}
			}
		return rounds;	
		}
	}
	
	public boolean isClassAttendedByStudent(int studentId, int classId) throws SQLException {
		int output;
		
		String query = "SELECT count(*) FROM attend WHERE idstudent = ? AND idclass = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, studentId);
			pstatement.setInt(2, classId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				output = result.getInt("count(*)");
			}
		}
		
		if (output == 1) return true;
		else return false;
	}
	
	public boolean doesClassExists(int classId)throws SQLException {
		int output;
		
		String query = "SELECT count(*) FROM class WHERE idclass = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, classId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				output = result.getInt("count(*)");
			}
		}
		
		if (output == 1) return true;
		else return false;
	}
	
	
	
	public boolean isClassTaughtByProfessor(int professorId, int classId) throws SQLException {
		int output;
		
		String query = "SELECT count(*) FROM class WHERE idprofessor = ? AND idclass = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, professorId);
			pstatement.setInt(2, classId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				output = result.getInt("count(*)");
			}
		}
		
		if (output == 1) return true;
		else return false;
	}

	
}