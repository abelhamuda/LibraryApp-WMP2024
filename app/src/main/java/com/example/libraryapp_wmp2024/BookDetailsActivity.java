package com.example.libraryapp_wmp2024;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvGenre = findViewById(R.id.tvGenre);
        TextView tvBarcode = findViewById(R.id.tvBarcode);

        // Ambil data dari Intent
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String genre = getIntent().getStringExtra("genre");
        String barcode = getIntent().getStringExtra("barcode");

        // Tampilkan data
        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvGenre.setText(genre);
        tvBarcode.setText(barcode);
    }
}
