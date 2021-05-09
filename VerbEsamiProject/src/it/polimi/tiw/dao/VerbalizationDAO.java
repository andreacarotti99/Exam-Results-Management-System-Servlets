package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.polimi.tiw.beans.Verbal;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class VerbalizationDAO {
	private Connection con;
	private int id;

	public VerbalizationDAO(Connection connection, int i) {
		this.con = connection;
		this.id = i;
	}

	/*
	 * creo un verbale in verbal con id autoincrement e data con timestamp e idround quello passato come parametro
	 * seleziono la tupla con quel timestamp e quell'idround da verbal
	 * faccio update nella tabella registred con quell'id autoincrement in quelli che hanno stato 2 o 3
	 * cambio lo stato di quelli che han stato 2 o 3 in 4
	 */


	//4
	public void changeStatusToVerbalizzato(Integer idround) throws SQLException {

		String query = "UPDATE registered SET state = 4 where idround = ? and (state = 2 or state = 3)";
	
		try (PreparedStatement pstatement = con.prepareStatement(query);) {

			pstatement.setInt(1, idround);
		
			pstatement.executeUpdate();
		}	
	}

	//3
	public void updateNewVerbalIdRegistered(Integer idround, Integer newVerbalId) throws SQLException {

		String query = "UPDATE registered SET idverbal = ? where idround = ? and (state = 2 or state = 3)";
	
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			
			pstatement.setInt(1, newVerbalId);

			pstatement.setInt(2, idround);
		
			pstatement.executeUpdate();
		}	
	}
	
	
	//2
	public int getTuplaGivenIdRoundAndTimestamp(Integer idround) throws SQLException {
		int verbalid = 0;
		String query2 = "SELECT * FROM verbal WHERE idround = ? ORDER BY idverbal DESC"; 
		//così scorriamo tutte le tuple fino all'ultima e la salviamo

		try (PreparedStatement pstatement = con.prepareStatement(query2);) {
			pstatement.setInt(1, idround);
			
		
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					
					verbalid = result.getInt("idverbal");
					
					System.out.println("verbalid: " + verbalid);

					
				}
			}
			
			return verbalid;
			
		}
		
	}


	//1
	public Timestamp createVerbalFromStatePubblicatoOrRifiutato(Integer idround) throws SQLException {
		Verbal verbal = new Verbal();
		Timestamp date = new Timestamp(new Date().getTime());
		
		System.out.println("First part: insert into db the new verbal and the timestamp...");
		//PRIMA PARTE DELLA QUERY
		
		String query = "INSERT INTO verbal (dateverbal, idround) VALUE (?, ?)";
		
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setTimestamp(1, date);
			pstatement.setInt(2, idround);
			pstatement.executeUpdate();
			System.out.println("First part correct...");
		}
		
		return date;
	}
	
	
}

