package com.example.libraryapp_wmp2024;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LibraryApp.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TAG = "DatabaseHelper";

    // Tabel Users untuk login
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_USERNAME = "username";
    private static final String COL_USER_PASSWORD = "password";

    // Tabel Books
    private static final String TABLE_BOOKS = "books";
    private static final String COL_BOOK_ID = "id";
    private static final String COL_BOOK_TITLE = "title";
    private static final String COL_BOOK_AUTHOR = "author";
    private static final String COL_BOOK_GENRE = "genre";
    private static final String COL_BOOK_BARCODE = "barcode";
    private static final String COL_BOOK_COVER_IMAGE = "cover_image";

    // Tabel Borrow
    private static final String TABLE_BORROW = "borrow";
    private static final String COL_BORROW_ID = "id";
    private static final String COL_BORROW_USERNAME = "username";
    private static final String COL_BORROW_BOOK_ID = "book_id";
    private static final String COL_BORROW_DATE = "borrow_date";
    private static final String COL_BORROW_RETURN_DATE = "return_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabel Users
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_USERNAME + " TEXT UNIQUE, "
                + COL_USER_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Tambahkan user default
        ContentValues defaultUser = new ContentValues();
        defaultUser.put(COL_USER_USERNAME, "admin");
        defaultUser.put(COL_USER_PASSWORD, "admin123");
        db.insert(TABLE_USERS, null, defaultUser);

        // Tabel Books
        String createBooksTable = "CREATE TABLE " + TABLE_BOOKS + " ("
                + COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_BOOK_TITLE + " TEXT, "
                + COL_BOOK_AUTHOR + " TEXT, "
                + COL_BOOK_GENRE + " TEXT, "
                + COL_BOOK_BARCODE + " TEXT UNIQUE, "
                + COL_BOOK_COVER_IMAGE + " TEXT, "
                + "UNIQUE(" + COL_BOOK_BARCODE + "))";
        db.execSQL(createBooksTable);

        // Tabel Borrow
        String createBorrowTable = "CREATE TABLE " + TABLE_BORROW + " ("
                + COL_BORROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_BORROW_USERNAME + " TEXT, "
                + COL_BORROW_BOOK_ID + " INTEGER, "
                + COL_BORROW_DATE + " TEXT, "
                + COL_BORROW_RETURN_DATE + " TEXT, "
                + "FOREIGN KEY(" + COL_BORROW_BOOK_ID + ") REFERENCES " + TABLE_BOOKS + "(" + COL_BOOK_ID + ") ON DELETE CASCADE)";
        db.execSQL(createBorrowTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BORROW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    // Tambah user (untuk registrasi atau menambahkan admin secara manual)
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_USER_USERNAME, username);
            values.put(COL_USER_PASSWORD, password);
            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error adding user: ", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    // Cek apakah username dan password sesuai (untuk login)
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_USERNAME + " = ? AND " + COL_USER_PASSWORD + " = ?";
            cursor = db.rawQuery(query, new String[]{username, password});
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error checking user: ", e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    // Tambah buku baru ke tabel Books
    public boolean addBook(String title, String author, String genre, String barcode, String coverImage) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_BOOK_TITLE, title);
            values.put(COL_BOOK_AUTHOR, author);
            values.put(COL_BOOK_GENRE, genre);
            values.put(COL_BOOK_BARCODE, barcode);
            values.put(COL_BOOK_COVER_IMAGE, coverImage);
            long result = db.insert(TABLE_BOOKS, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error adding book: ", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    // Update buku berdasarkan ID
    public boolean updateBook(int bookId, String title, String author, String genre, String barcode, String coverImage) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_BOOK_TITLE, title);
            values.put(COL_BOOK_AUTHOR, author);
            values.put(COL_BOOK_GENRE, genre);
            values.put(COL_BOOK_BARCODE, barcode);
            values.put(COL_BOOK_COVER_IMAGE, coverImage);
            int result = db.update(TABLE_BOOKS, values, COL_BOOK_ID + " = ?", new String[]{String.valueOf(bookId)});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating book: ", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    // Hapus buku berdasarkan ID
    public boolean deleteBookById(int bookId) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int result = db.delete(TABLE_BOOKS, COL_BOOK_ID + " = ?", new String[]{String.valueOf(bookId)});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting book: ", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    // Mengambil semua data buku dari tabel Books
    public Cursor getAllBooks() {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_BOOKS;
            return db.rawQuery(query, null);
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving books: ", e);
            return null;
        }
    }

//    // Cek apakah buku sudah dipinjam
//    public boolean isBookBorrowed(int bookId) {
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//            db = this.getReadableDatabase();
//            String query = "SELECT * FROM " + TABLE_BORROW + " WHERE " + COL_BORROW_BOOK_ID + " = ? AND " + COL_BORROW_RETURN_DATE + " IS NULL";
//            cursor = db.rawQuery(query, new String[]{String.valueOf(bookId)});
//            return cursor.getCount() > 0;  // Jika ada pinjaman yang belum dikembalikan
//        } catch (Exception e) {
//            Log.e(TAG, "Error checking if book is borrowed: ", e);
//            return false;
//        } finally {
//            if (cursor != null) cursor.close();
//            if (db != null) db.close();
//        }
//    }
//
//
//    // Cek apakah buku sudah dipinjam
//    public boolean isBookBorrowed(int bookId) {
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//        try {
//            db = this.getReadableDatabase();
//            String query = "SELECT * FROM " + TABLE_BORROW + " WHERE " + COL_BORROW_BOOK_ID + " = ? AND " + COL_BORROW_RETURN_DATE + " IS NULL";
//            cursor = db.rawQuery(query, new String[]{String.valueOf(bookId)});
//            return cursor.getCount() > 0;  // Jika ada pinjaman yang belum dikembalikan
//        } catch (Exception e) {
//            Log.e(TAG, "Error checking if book is borrowed: ", e);
//            return false;
//        } finally {
//            if (cursor != null) cursor.close();
//            if (db != null) db.close();
//        }
//    }


    // Tambah data peminjaman ke tabel Borrow
    public boolean addBorrow(String username, int bookId, String borrowDate, String returnDate) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_BORROW_USERNAME, username);
            values.put(COL_BORROW_BOOK_ID, bookId);
            values.put(COL_BORROW_DATE, borrowDate);
            values.put(COL_BORROW_RETURN_DATE, returnDate);
            long result = db.insert(TABLE_BORROW, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error adding borrow record: ", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    // Pinjam buku
    public boolean borrowBook(String username, int bookId, String borrowDate, String returnDate) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_BORROW_USERNAME, username);
            values.put(COL_BORROW_BOOK_ID, bookId);
            values.put(COL_BORROW_DATE, borrowDate);
            values.put(COL_BORROW_RETURN_DATE, returnDate);
            long result = db.insert(TABLE_BORROW, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error borrowing book: ", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    // Tambahkan metode getBookByBarcode di DatabaseHelper
    public Book getBookByBarcode(String barcode) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + COL_BOOK_BARCODE + " = ?";
            cursor = db.rawQuery(query, new String[]{barcode});
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_AUTHOR));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_GENRE));
                String coverImage = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_COVER_IMAGE));

                return new Book(id, title, author, genre, barcode, coverImage);
            } else {
                return null; // Jika tidak ada buku yang ditemukan
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving book by barcode: ", e);
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    // Kembalikan buku
    public boolean returnBook(int borrowId, String returnDate) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_BORROW_RETURN_DATE, returnDate);
            int result = db.update(TABLE_BORROW, values, COL_BORROW_ID + " = ?", new String[]{String.valueOf(borrowId)});
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error returning book: ", e);
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    // Mengambil semua data pinjaman buku berdasarkan username
    public Cursor getUserBorrowedBooks(String username) {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_BORROW + " WHERE " + COL_BORROW_USERNAME + " = ?";
            return db.rawQuery(query, new String[]{username});
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving borrowed books for user: ", e);
            return null;
        }
    }

    // Mengambil data buku berdasarkan ID
    public Cursor getBookById(int bookId) {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + COL_BOOK_ID + " = ?";
            return db.rawQuery(query, new String[]{String.valueOf(bookId)});
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving book by ID: ", e);
            return null;
        }
    }
}

