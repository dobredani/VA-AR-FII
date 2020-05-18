package com.example.myapplication.Schedule;


public class SchedModel {
    String courseTitle;
    String studentGroup;
    String startHour;
    String endHour;

    public SchedModel(String courseTitle, String studentGroup, String startHour, String endHour) {
        this.courseTitle = courseTitle;
        this.studentGroup = studentGroup;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public String getHourInterval() {
        return startHour + '-' + endHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getStartHour() {
        return startHour;
    }
}