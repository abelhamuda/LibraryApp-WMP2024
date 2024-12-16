package com.example.libraryapp_wmp2024;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    // Gunakan ActivityResultLauncher untuk menangani hasil pemindaian
    private final ActivityResultLauncher<Intent> barcodeLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
                    if (scanResult != null && scanResult.getContents() != null) {
                        String scannedBarcode = scanResult.getContents();

                        // Cari buku berdasarkan barcode
                        Book book = databaseHelper.getBookByBarcode(scannedBarcode);
                        if (book != null) {
                            // Kirim data buku ke BookDetailsActivity
                            Intent intent = new Intent(this, BookDetailsActivity.class);
                            intent.putExtra("title", book.getTitle());
                            intent.putExtra("author", book.getAuthor());
                            intent.putExtra("genre", book.getGenre());
                            intent.putExtra("barcode", book.getBarcode());
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Buku tidak ditemukan!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Pemindaian dibatalkan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Pemindaian gagal atau dibatalkan", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);

        // Mulai pemindaian barcode
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan kode barcode buku");
        integrator.setBeepEnabled(true);
        barcodeLauncher.launch(integrator.createScanIntent());
    }
}
