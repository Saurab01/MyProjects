package com.personal.saurabh.archive.domain;

import java.io.Serializable;

/**
 * Created by saurabhagrawal on 19/02/17.
 */
public class Student  implements Serializable {
    int stno;
    String sname;
    float marks;
    transient String password;  //Now it will not be serialized

    public Student(int stno, String sname, float marks, String password) {
        this.stno = stno;
        this.sname = sname;
        this.marks = marks;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stno=" + stno +
                ", sname='" + sname + '\'' +
                ", marks=" + marks +
                ", password='" + password + '\'' +
                '}';
    }

    //getters and setters
    public int getStno() {
        return stno;
    }

    public void setStno(int stno) {
        this.stno = stno;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public float getMarks() {
        return marks;
    }

    public void setMarks(float marks) {
        this.marks = marks;
    }
}
