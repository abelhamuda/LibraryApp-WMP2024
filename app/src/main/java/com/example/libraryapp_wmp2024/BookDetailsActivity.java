package com.example.libraryapp_wmp2024;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.Glide;

public class BookDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);  // Pastikan layout sudah disesuaikan

        // Inisialisasi tombol back
        AppCompatImageView backButton = findViewById(R.id.toolbar_back_button);

        // Inisialisasi View
        ImageView ivBookCover = findViewById(R.id.ivBookCover);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvGenre = findViewById(R.id.tvGenre);
        TextView tvBarcode = findViewById(R.id.tvBarcode);
        Button btnBorrowBook = findViewById(R.id.btnBorrowBook);

        // Mengambil data dari Intent
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String genre = getIntent().getStringExtra("genre");
        String barcode = getIntent().getStringExtra("barcode");
        String coverImagePath = getIntent().getStringExtra("coverImagePath");

        // Menampilkan data pada View
        tvTitle.setText(title != null ? title : "Tidak tersedia");
        tvAuthor.setText(author != null ? author : "Tidak tersedia");
        tvGenre.setText(genre != null ? genre : "Tidak tersedia");
        tvBarcode.setText(barcode != null ? barcode : "Tidak tersedia");

        // Load gambar dengan Glide (atau library lain) untuk cover buku
        if (coverImagePath != null && !coverImagePath.isEmpty()) {
            Glide.with(this)
                    .load(coverImagePath)
                    .into(ivBookCover);
        } else {
            ivBookCover.setImageResource(R.drawable.placeholder_image); // Menampilkan placeholder jika gambar tidak ada
        }

        // Event tombol "Pinjam Buku"
        btnBorrowBook.setOnClickListener(v -> {
            Toast.makeText(this, "Buku berhasil dipinjam!", Toast.LENGTH_SHORT).show();
        });

        // Event untuk Back Button
        backButton.setOnClickListener(v -> onBackPressed());
    }
}
