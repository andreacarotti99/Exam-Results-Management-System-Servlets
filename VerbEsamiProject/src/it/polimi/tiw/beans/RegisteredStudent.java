package it.polimi.tiw.beans;

import it.polimi.tiw.beans.MarkStatus;

public class RegisteredStudent {
    private int id;
    private String username;
    private String name;
    private String surname;
    private String mail;
    private Integer studentNumber;
    private String degreeCourse;
    private Integer mark;
    private MarkStatus status;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
 
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMail() {
        return mail;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public String getDegreeCourse() {
        return degreeCourse;
    }

    public Integer getMark() {
        return mark;
    }

    public MarkStatus getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setDegreeCourse(String degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public void setStatus(Integer status) {
		this.status = MarkStatus.getMarkStatusFromInt(status);
    }
}

