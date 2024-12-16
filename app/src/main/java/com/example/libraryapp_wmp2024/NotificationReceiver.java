package com.example.libraryapp_wmp2024;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "LibraryAppChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Ambil data dari Intent
        String bookTitle = intent.getStringExtra("bookTitle");

        // Buat NotificationChannel (dibutuhkan untuk API 26+)
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Library Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Buat notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Tambahkan ikon notifikasi di drawable
                .setContentTitle("Pengingat Pengembalian Buku")
                .setContentText("Jangan lupa mengembalikan buku: " + bookTitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Tampilkan notifikasi
        notificationManager.notify(1, builder.build());
    }
}