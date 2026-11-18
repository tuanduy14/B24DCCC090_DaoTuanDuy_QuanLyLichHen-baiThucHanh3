package com.example.quanlylichhen;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class AppointmentDetailActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private int id;
    private TextView tvReminderState;
    private SwitchCompat swReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        db = new DatabaseHelper(this);
        findViewById(R.id.btnBackTop).setOnClickListener(v -> finish());
        id = getIntent().getIntExtra("id", -1);

        Appointment a = db.getAppointmentById(id);
        if (a == null) {
            Toast.makeText(this, "Không tìm thấy lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ((TextView) findViewById(R.id.tvTitle)).setText(a.getTitle());
        ((TextView) findViewById(R.id.tvLocation)).setText(a.getLocation());
        ((TextView) findViewById(R.id.tvDateTime)).setText(a.getDisplayDateTime());
        tvReminderState = findViewById(R.id.tvReminderState);
        swReminder = findViewById(R.id.swReminder);

        swReminder.setChecked(a.isReminder());
        updateState(a.isReminder());
        swReminder.setOnCheckedChangeListener((btn, checked) -> {
            db.updateReminder(id, checked);
            updateState(checked);
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            db.deleteAppointment(id);
            Toast.makeText(this, "Đã xóa lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateState(boolean on) {
        tvReminderState.setText(on ? "Đang bật" : "Đang tắt");
    }
}
