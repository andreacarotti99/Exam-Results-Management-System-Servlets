package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EditMarkDAO {
	private Connection con;
	private int id;

	public EditMarkDAO(Connection connection, int i) {
		this.con = connection;
		this.id = i;
	}


	public void createNewMark(Integer newMark, Integer idstudent, Integer idround) throws SQLException {		
		
		String query = "UPDATE registered SET mark = ? where idstudent = ? and idround = ?";
		
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, newMark);
			pstatement.setInt(2, idstudent);
			pstatement.setInt(3, idround);
			pstatement.executeUpdate();
		}
	
	}
	
	public void changeToInserito(Integer idstudent, Integer idround) throws SQLException {		

		String query = "UPDATE registered SET state = 1 where idstudent = ? and idround = ?";
		
		try (PreparedStatement pstatement = con.prepareStatement(query);) {

			pstatement.setInt(1, idstudent);
			pstatement.setInt(2, idround);
			
			pstatement.executeUpdate();
		}
	}

	
	public void changeStatusToPubblicato(Integer idround) throws SQLException {		

		String query = "UPDATE registered SET state = 2 where idround = ?";
		
		try (PreparedStatement pstatement = con.prepareStatement(query);) {

			pstatement.setInt(1, idround);
			
			pstatement.executeUpdate();
		}
	}
	
	
	
}