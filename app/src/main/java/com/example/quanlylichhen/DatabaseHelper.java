package com.example.quanlylichhen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "appointments.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, password TEXT NOT NULL)");
        db.execSQL("CREATE TABLE appointments (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, location TEXT NOT NULL, date TEXT NOT NULL, " +
                "time TEXT NOT NULL, reminder INTEGER NOT NULL)");

        insertUser(db, "admin", "123456");
        insertAppointment(db, new Appointment(0, "Họp nhóm đồ án", "Phòng B203", "2026-10-25", "09:00", true));
        insertAppointment(db, new Appointment(0, "Khám răng", "Nha khoa Minh Tâm", "2026-10-26", "14:30", true));
        insertAppointment(db, new Appointment(0, "Đi cà phê với bạn", "Highland Coffee", "2026-10-27", "19:00", false));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS appointments");
        onCreate(db);
    }

    // ---------- users ----------
    private long insertUser(SQLiteDatabase db, String username, String password) {
        ContentValues v = new ContentValues();
        v.put("username", username);
        v.put("password", password);
        return db.insert("users", null, v);
    }

    public long insertUser(String username, String password) {
        return insertUser(getWritableDatabase(), username, password);
    }

    public boolean checkLogin(String username, String password) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id FROM users WHERE username = ? AND password = ?",
                new String[]{username, password});
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    // ---------- appointments ----------
    private long insertAppointment(SQLiteDatabase db, Appointment a) {
        ContentValues v = new ContentValues();
        v.put("title", a.getTitle());
        v.put("location", a.getLocation());
        v.put("date", a.getDate());
        v.put("time", a.getTime());
        v.put("reminder", a.isReminder() ? 1 : 0);
        return db.insert("appointments", null, v);
    }

    public long insertAppointment(Appointment a) {
        return insertAppointment(getWritableDatabase(), a);
    }

    private Appointment fromCursor(Cursor c) {
        return new Appointment(c.getInt(0), c.getString(1), c.getString(2),
                c.getString(3), c.getString(4), c.getInt(5) == 1);
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, title, location, date, time, reminder FROM appointments " +
                        "ORDER BY date ASC, time ASC", null);
        while (c.moveToNext()) list.add(fromCursor(c));
        c.close();
        return list;
    }

    public Appointment getAppointmentById(int id) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, title, location, date, time, reminder FROM appointments WHERE id = ?",
                new String[]{String.valueOf(id)});
        Appointment a = c.moveToFirst() ? fromCursor(c) : null;
        c.close();
        return a;
    }

    public int updateReminder(int id, boolean on) {
        ContentValues v = new ContentValues();
        v.put("reminder", on ? 1 : 0);
        return getWritableDatabase().update("appointments", v, "id = ?",
                new String[]{String.valueOf(id)});
    }

    public int deleteAppointment(int id) {
        return getWritableDatabase().delete("appointments", "id = ?",
                new String[]{String.valueOf(id)});
    }

    public int countAll() {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM appointments", null);
        c.moveToFirst();
        int n = c.getInt(0);
        c.close();
        return n;
    }

    public int countReminders() {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM appointments WHERE reminder = 1", null);
        c.moveToFirst();
        int n = c.getInt(0);
        c.close();
        return n;
    }
}
