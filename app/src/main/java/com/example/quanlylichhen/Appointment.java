package com.example.quanlylichhen;

public class Appointment {
    private int id;
    private String title, location, date, time; // date: yyyy-MM-dd, time: HH:mm
    private boolean reminder;

    public Appointment(int id, String title, String location, String date, String time, boolean reminder) {
        this.id = id; this.title = title; this.location = location;
        this.date = date; this.time = time; this.reminder = reminder;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public boolean isReminder() { return reminder; }

    // yyyy-MM-dd -> dd/MM/yyyy · HH:mm
    public String getDisplayDateTime() {
        String[] p = date.split("-");
        return p[2] + "/" + p[1] + "/" + p[0] + " · " + time;
    }
}
