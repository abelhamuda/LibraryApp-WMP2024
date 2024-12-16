package com.example.libraryapp_wmp2024;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.io.IOException;

public class AddBookActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etGenre, etBarcode;
    private ImageView ivCoverImage;
    private Uri selectedImageUri; // Untuk menyimpan URI gambar yang dipilih

    private DatabaseHelper databaseHelper;

    private ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    ivCoverImage.setImageURI(selectedImageUri); // Tampilkan gambar di ImageView
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etGenre = findViewById(R.id.etGenre);
        etBarcode = findViewById(R.id.etBarcode);
        ivCoverImage = findViewById(R.id.ivCoverImage);

        Button btnChooseCover = findViewById(R.id.btnChooseCover);
        Button btnSaveBook = findViewById(R.id.btnSaveBook);

        databaseHelper = new DatabaseHelper(this);

        // Pilih gambar cover dari galeri
        btnChooseCover.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        // Simpan buku ke database
        btnSaveBook.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String author = etAuthor.getText().toString();
            String genre = etGenre.getText().toString();
            String barcode = etBarcode.getText().toString();

            // Pastikan semua input tidak kosong
            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || barcode.isEmpty()) {
                Toast.makeText(AddBookActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String coverImagePath = selectedImageUri != null ? selectedImageUri.toString() : ""; // Menyimpan path gambar sebagai String

            // Tambahkan buku ke database
            boolean isBookAdded = databaseHelper.addBook(title, author, genre, barcode, coverImagePath);

            if (isBookAdded) {
                Toast.makeText(AddBookActivity.this, "Book Added Successfully", Toast.LENGTH_SHORT).show();
                // Kembali ke HomeActivity setelah menambahkan buku
                finish();
            } else {
                Toast.makeText(AddBookActivity.this, "Failed to Add Book", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
