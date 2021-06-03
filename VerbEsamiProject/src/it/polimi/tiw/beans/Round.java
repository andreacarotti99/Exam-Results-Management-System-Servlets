package it.polimi.tiw.beans;

import java.util.Date;

public class Round {

	private int roundId;
	private Date date;
	private String className;
	
	
	public int getRoundId() {
		return roundId;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
}
