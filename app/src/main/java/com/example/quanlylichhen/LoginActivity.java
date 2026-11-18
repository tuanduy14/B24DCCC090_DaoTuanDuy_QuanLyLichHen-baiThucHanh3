package com.example.quanlylichhen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class LoginActivity extends AppCompatActivity {
    private EditText etUser, etPass;
    private TextView tvError;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // giữ giao diện sáng
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        tvError = findViewById(R.id.tvError);
        Button btn = findViewById(R.id.btnLogin);
        btn.setOnClickListener(v -> login());

        etPass.setOnEditorActionListener((v, actionId, e) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                login();
                return true;
            }
            return false;
        });
    }

    private void login() {
        String u = etUser.getText().toString().trim();
        String p = etPass.getText().toString();
        if (u.isEmpty() || p.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập và mật khẩu");
            return;
        }
        if (db.checkLogin(u, p)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            showError("Sai tên đăng nhập hoặc mật khẩu");
        }
    }

    private void showError(String msg) {
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
    }
}
