package it.polimi.tiw.beans;

public class Classe {

	private int classID;
	private String className;
	private int professorID;
	
	
	
	public int getClassID() {
		return this.classID;
	}
	public String getClassName() {
		return this.className;
	}
	public int getProfessorID() {
		return this.professorID;
	}
	
	
	public void setClassID (int classID) {
		this.classID = classID;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setProfessorID(int professorID) {
		this.professorID = professorID;
	}
	
	
}
