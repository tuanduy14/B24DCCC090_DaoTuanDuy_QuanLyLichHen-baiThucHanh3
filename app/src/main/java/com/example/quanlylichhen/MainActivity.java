package com.example.quanlylichhen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextView tvStats;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        tvStats = findViewById(R.id.tvStats);
        lv = findViewById(R.id.lvAppointments);
        lv.setEmptyView(findViewById(R.id.tvEmpty));

        lv.setOnItemClickListener((parent, view, pos, id) -> {
            Appointment a = (Appointment) parent.getItemAtPosition(pos);
            Intent i = new Intent(this, AppointmentDetailActivity.class);
            i.putExtra("id", a.getId());
            startActivity(i);
        });

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddAppointmentActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Appointment> list = db.getAllAppointments();
        lv.setAdapter(new AppointmentAdapter(this, list));
        tvStats.setText("Tổng: " + db.countAll() + " lịch hẹn  |  Đang bật nhắc nhở: " + db.countReminders());
    }
}
