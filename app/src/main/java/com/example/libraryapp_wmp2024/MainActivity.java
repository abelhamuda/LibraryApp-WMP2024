package com.example.libraryapp_wmp2024;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Referensi elemen UI
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Event klik tombol Login
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim(); // Menghapus spasi tambahan
            String password = etPassword.getText().toString().trim();

            // Validasi input
            if (TextUtils.isEmpty(username)) {
                etUsername.setError("Username tidak boleh kosong!");
                etUsername.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Password tidak boleh kosong!");
                etPassword.requestFocus();
                return;
            }

            // Validasi pengguna di database
            boolean isValid = databaseHelper.checkUser(username, password);
            if (isValid) {
                // Login berhasil
                Toast.makeText(this, "Login berhasil! Selamat datang, " + username + "!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish(); // Tutup aktivitas login
            } else {
                // Login gagal
                Toast.makeText(this, "Username atau password salah! Coba lagi.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
