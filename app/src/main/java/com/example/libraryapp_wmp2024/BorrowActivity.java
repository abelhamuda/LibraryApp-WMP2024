package com.example.libraryapp_wmp2024;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class BorrowActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private int selectedBookId = -1; // Menyimpan ID buku yang dipilih
    private EditText etReturnDate;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        databaseHelper = new DatabaseHelper(this);

        Spinner bookSpinner = findViewById(R.id.spinnerBooks);
        etReturnDate = findViewById(R.id.etReturnDate);
        Button btnBorrow = findViewById(R.id.btnBorrow);
        calendar = Calendar.getInstance();

        // Mengisi spinner dengan daftar buku
        ArrayList<String> bookTitles = new ArrayList<>();
        HashMap<String, Integer> bookMap = new HashMap<>(); // Map untuk menyimpan hubungan antara judul buku dan ID
        Cursor cursor = databaseHelper.getAllBooks();
        while (cursor.moveToNext()) {
            int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            bookTitles.add(title);
            bookMap.put(title, bookId); // Simpan ID berdasarkan judul
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookSpinner.setAdapter(adapter);

        // Mengatur listener untuk spinner
        bookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTitle = bookTitles.get(position);
                selectedBookId = bookMap.get(selectedTitle); // Ambil ID buku berdasarkan judul yang dipilih
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedBookId = -1; // Reset jika tidak ada yang dipilih
            }
        });

        // Listener untuk memilih tanggal kembali
        etReturnDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(BorrowActivity.this, (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                etReturnDate.setText(sdf.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Listener untuk tombol pinjam
        btnBorrow.setOnClickListener(v -> {
            String userName = getIntent().getStringExtra("username"); // Ambil username dari intent
            String returnDate = etReturnDate.getText().toString();

            // Validasi input
            if (selectedBookId != -1 && userName != null && !returnDate.isEmpty()) {
                boolean success = databaseHelper.addBorrow(userName, selectedBookId, getCurrentDate(), returnDate);
                if (success) {
                    Toast.makeText(BorrowActivity.this, "Peminjaman berhasil!", Toast.LENGTH_SHORT).show();
                    finish(); // Tutup aktivitas setelah sukses
                } else {
                    Toast.makeText(BorrowActivity.this, "Peminjaman gagal. Cek log untuk detail.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(BorrowActivity.this, "Harap pilih buku, masukkan tanggal kembali, dan pastikan username valid.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Mendapatkan tanggal saat ini dalam format "yyyy-MM-dd"
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(Calendar.getInstance().getTime());
    }
}
