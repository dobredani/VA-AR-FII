package com.example.myapplication.problem;

public class Lecture {
    String group;
    String course;
    String dayOfWeek;
    String startTime;
    String finishTime;

   int dayNumer = 0;

    public int getDayNumer() {
        return dayNumer;
    }

    public Lecture(String group, String course, String dayOfWeek, String startTime, String finishTime) {
        this.group = group;
        this.course = course;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.finishTime = finishTime;

        if (dayOfWeek.equalsIgnoreCase("Luni")) dayNumer = 1;
        if (dayOfWeek.equalsIgnoreCase("Marti")) dayNumer = 2;
        if (dayOfWeek.equalsIgnoreCase("Miercuri")) dayNumer = 3;
        if (dayOfWeek.equalsIgnoreCase("Joi")) dayNumer = 4;
        if (dayOfWeek.equalsIgnoreCase("Vineri")) dayNumer = 5;
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
