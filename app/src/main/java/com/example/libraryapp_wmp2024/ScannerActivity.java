package com.example.libraryapp_wmp2024;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi database helper
        databaseHelper = new DatabaseHelper(this);

        // Mulai pemindaian barcode
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan kode barcode buku");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true); // Pastikan orientasi tetap saat pemindaian
        integrator.initiateScan(); // Mulai pemindaian
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Proses hasil pemindaian
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            if (scanResult.getContents() != null) {
                String scannedBarcode = scanResult.getContents();

                // Cari buku berdasarkan barcode di database
                Book book = databaseHelper.getBookByBarcode(scannedBarcode);
                if (book != null) {
                    // Jika buku ditemukan, buka BookDetailsActivity dengan data buku
                    Intent intent = new Intent(this, BookDetailsActivity.class);
                    intent.putExtra("title", book.getTitle());
                    intent.putExtra("author", book.getAuthor());
                    intent.putExtra("genre", book.getGenre());
                    intent.putExtra("barcode", book.getBarcode());
                    intent.putExtra("coverImagePath", book.getCoverImagePath());
                    startActivity(intent);
                    finish(); // Tutup ScannerActivity
                } else {
                    // Jika buku tidak ditemukan
                    Toast.makeText(this, "Buku tidak ditemukan di database!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Jika pemindaian dibatalkan
                Toast.makeText(this, "Pemindaian dibatalkan", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Jika tidak ada hasil pemindaian
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}