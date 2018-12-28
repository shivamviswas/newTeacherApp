package com.wikav.teahcer.teacherApp;

public class AttendanceItemList {
    private String name_Of_Student;
    private String attendance_of_student;

    public AttendanceItemList(String name_Of_Student, String attendance_of_student) {
        this.name_Of_Student = name_Of_Student;
        this.attendance_of_student = attendance_of_student;
    }

    public String getName_Of_Student() {
        return name_Of_Student;
    }

    public String getAttendance_of_student() {
        return attendance_of_student;
    }
}
