package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneralChecksDAO {
	private Connection connection;

	public GeneralChecksDAO(Connection connection) {
		this.connection = connection;
	}
	
	
	public boolean isClassTaughtByProfessor(int professorId, int classId) throws SQLException {
		int output = 0;
		
		String query = "SELECT count(*) FROM class WHERE idprofessor = ? AND idclass = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, professorId);
			pstatement.setInt(2, classId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					output = result.getInt("count(*)");
				}
			}
		}
		
		if (output == 1) return true;
		else return false;
	}
	
	
	public boolean isClassAttendedByStudent(int studentId, int classId) throws SQLException {
		int output = 0;
		
		String query = "SELECT count(*) FROM attend WHERE idstudent = ? AND idclass = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, studentId);
			pstatement.setInt(2, classId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					output = result.getInt("count(*)");
				}
			}
		}
		
		if (output == 1) return true;
		else return false;
	}
	
	
	public boolean isRoundOfThisProfessor(int professorId, int roundId) throws SQLException {
		int output = 0;
		
		String query = "SELECT count(*) FROM class c join round r on c.idclass = r.idclass WHERE c.idprofessor = ? AND r.idround = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, professorId);
			pstatement.setInt(2, roundId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					output = result.getInt("count(*)");
				}
			}
		}
		
		if (output == 1) return true;
		else return false;
	}
	
	
	public boolean isStudentRegisteredToThisRound(int studentId, int roundId) throws SQLException {
		int output = 0;
		
		String query = "SELECT count(*) FROM registered WHERE idstudent = ? AND idround = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, studentId);
			pstatement.setInt(2, roundId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					output = result.getInt("count(*)");
				}
			}
		}
		
		if (output == 1) return true;
		else return false;
	}
	
	
	
	public boolean isRoundVerbalizable(int roundId) throws SQLException {
		int output = 0;
		
		String query = "SELECT count(*) FROM registered WHERE idround = ? AND (state = 2 OR state = 3)";  //state 2 is published and state 3 is rejected
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, roundId);
			
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					output = result.getInt("count(*)");
				}
			}
		}
		
		if (output >= 1) return true;
		else return false;
	}
	
	
}
