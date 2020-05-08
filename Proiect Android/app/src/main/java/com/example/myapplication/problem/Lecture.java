package com.example.myapplication.problem;

public class Lecture {
    String group;
    String course;
    String dayOfWeek;
    String startTime;
    String finishTime;

    public Lecture(String group, String course, String dayOfWeek, String startTime, String finishTime) {
        this.group = group;
        this.course = course;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public String getGroup() {
        return group;
    }

    public String getCourse() {
        return course;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "group='" + group + '\'' +
                ", course='" + course + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime='" + startTime + '\'' +
                ", finishTime='" + finishTime + '\'' +
                '}';
    }
}
