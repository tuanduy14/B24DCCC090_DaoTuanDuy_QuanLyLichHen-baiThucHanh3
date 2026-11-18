package com.example.quanlylichhen;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Calendar;
import java.util.Locale;

public class AddAppointmentActivity extends AppCompatActivity {
    private EditText etTitle, etLocation;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private SwitchCompat swReminder;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        db = new DatabaseHelper(this);
        findViewById(R.id.btnBackTop).setOnClickListener(v -> finish());
        etTitle = findViewById(R.id.etTitle);
        etLocation = findViewById(R.id.etLocation);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        swReminder = findViewById(R.id.swReminder);
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> save());

        // Nhấn Enter / Done ở ô nhập -> tắt bàn phím
        EditText.OnEditorActionListener doneListener = (v, actionId, e) -> {
            boolean enter = e != null && e.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && e.getAction() == KeyEvent.ACTION_DOWN;
            if (actionId == EditorInfo.IME_ACTION_DONE || enter) {
                hideKeyboard(v);
                return true;
            }
            return false;
        };
        etTitle.setOnEditorActionListener(doneListener);
        etLocation.setOnEditorActionListener(doneListener);
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        v.clearFocus();
    }

    private void save() {
        String title = etTitle.getText().toString().trim();
        String loc = etLocation.getText().toString().trim();
        if (title.isEmpty()) { etTitle.setError("Không được để trống"); return; }
        if (loc.isEmpty()) { etLocation.setError("Không được để trống"); return; }

        int y = datePicker.getYear(), m = datePicker.getMonth(), d = datePicker.getDayOfMonth();
        int h = timePicker.getHour(), min = timePicker.getMinute();

        Calendar when = Calendar.getInstance();
        when.set(y, m, d, h, min, 0);
        if (!when.after(Calendar.getInstance())) {
            Toast.makeText(this, "Thời điểm hẹn phải sau thời điểm hiện tại", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d);
        String time = String.format(Locale.US, "%02d:%02d", h, min);
        db.insertAppointment(new Appointment(0, title, loc, date, time, swReminder.isChecked()));
        Toast.makeText(this, "Đã lưu lịch hẹn", Toast.LENGTH_SHORT).show();
        finish();
    }
}
