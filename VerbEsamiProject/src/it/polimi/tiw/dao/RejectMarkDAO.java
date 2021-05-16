package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class RejectMarkDAO {
	private Connection con;
	private int id;

	public RejectMarkDAO(Connection connection, int i) {
		this.con = connection;
		this.id = i;
	}


	public void rejectMark(Integer idstudent, Integer idround) throws SQLException {		
		
		String query = "UPDATE registered SET state = 3 where idstudent = ? and idround = ?";
		
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, idstudent);
			pstatement.setInt(2, idround);
			pstatement.executeUpdate();
		}
	
	}
	
}
