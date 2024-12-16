package com.example.libraryapp_wmp2024;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_AUTHOR = "author";
    private static final String EXTRA_GENRE = "genre";
    private static final String EXTRA_COVER_IMAGE_PATH = "coverImagePath";

    private DatabaseHelper databaseHelper;
    private ArrayList<Book> bookList;
    private BookAdapter adapter;
    private TextView emptyListMessage;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String scannedCode = result.getContents();
                    Log.d(TAG, "Scanned Code: " + scannedCode);

                    Book book = databaseHelper.getBookByBarcode(scannedCode);
                    if (book != null) {
                        Intent intent = new Intent(HomeActivity.this, BookDetailsActivity.class);
                        intent.putExtra(EXTRA_TITLE, book.getTitle());
                        intent.putExtra(EXTRA_AUTHOR, book.getAuthor());
                        intent.putExtra(EXTRA_GENRE, book.getGenre());
                        intent.putExtra(EXTRA_COVER_IMAGE_PATH, book.getCoverImagePath());
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnAddBook = findViewById(R.id.btnAddBook);
        Button btnScanBook = findViewById(R.id.btnScanBook);
        ListView bookListView = findViewById(R.id.bookListView);
        emptyListMessage = findViewById(R.id.emptyListMessage);

        String username = getIntent().getStringExtra("username");

        // Hanya admin yang bisa menambah buku
        if (!"admin".equals(username)) {
            btnAddBook.setVisibility(View.GONE);
        }

        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddBookActivity.class);
            startActivity(intent);
        });

        databaseHelper = new DatabaseHelper(this);
        bookList = new ArrayList<>();

        // Mengambil data buku dari database
        try (Cursor cursor = databaseHelper.getAllBooks()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                    String genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"));
                    String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
                    String coverImagePath = cursor.getString(cursor.getColumnIndexOrThrow("cover_image"));

                    bookList.add(new Book(title, author, genre, barcode, coverImagePath));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching books from database", e);
            Toast.makeText(this, "Gagal mengambil data buku", Toast.LENGTH_SHORT).show();
        }

        // Menampilkan daftar buku atau pesan kosong
        if (bookList.isEmpty()) {
            emptyListMessage.setVisibility(View.VISIBLE);
            bookListView.setVisibility(View.GONE);
        } else {
            emptyListMessage.setVisibility(View.GONE);
            adapter = new BookAdapter(this, bookList);
            bookListView.setAdapter(adapter);
        }

        btnScanBook.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Arahkan kamera ke kode QR/Barcode");
            options.setBeepEnabled(true);
            options.setBarcodeImageEnabled(true);
            barcodeLauncher.launch(options);
        });
    }
}
