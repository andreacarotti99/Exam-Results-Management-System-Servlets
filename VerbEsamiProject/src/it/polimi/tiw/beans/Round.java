package it.polimi.tiw.beans;

import java.util.Date;

public class Round {

	private int roundID;
	private Date date;
	private int classID;
	private int professorID;
	private int studentID;
	
	public int getStudentID() {
		return this.studentID;
	}
	
	public int getProfessorID() {
		return this.professorID;
	}
	
	public int getRoundID() {
		return this.roundID;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public int getClassID() {
		return this.classID;
	}
	
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	
	public void setProfessorID(int professorID) {
		this.professorID = professorID;
	}
	
	public void setClassID (int classID) {
		this.classID = classID;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public void setRoundID(int roundID) {
		this.roundID = roundID;
	}
	
	
}
