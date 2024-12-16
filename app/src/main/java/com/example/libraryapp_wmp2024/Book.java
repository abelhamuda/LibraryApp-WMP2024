package com.example.libraryapp_wmp2024;

/**
 * Representasi data Buku di dalam aplikasi perpustakaan.
 * Menyimpan informasi penting seperti judul, penulis, genre, barcode, gambar sampul, dan ID buku.
 */
public class Book {

    // Fields untuk menyimpan data buku
    private int id; // ID unik untuk buku (INTEGER sesuai database)
    private String title;
    private String author;
    private String genre;
    private String barcode;
    private String coverImagePath;

    /**
     * Konstruktor tanpa ID (misalnya untuk buku yang baru akan dimasukkan ke database).
     * @param title Judul buku
     * @param author Penulis buku
     * @param genre Genre buku
     * @param barcode Barcode buku
     * @param coverImagePath Jalur gambar sampul buku
     */
    public Book(String title, String author, String genre, String barcode, String coverImagePath) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (barcode == null || barcode.isEmpty()) {
            throw new IllegalArgumentException("Barcode cannot be null or empty");
        }

        this.title = title;
        this.author = author;
        this.genre = genre;
        this.barcode = barcode;
        this.coverImagePath = coverImagePath;
    }

    /**
     * Konstruktor dengan ID (misalnya untuk buku yang sudah ada di database).
     * @param id ID unik buku
     * @param title Judul buku
     * @param author Penulis buku
     * @param genre Genre buku
     * @param barcode Barcode buku
     * @param coverImagePath Jalur gambar sampul buku
     */
    public Book(int id, String title, String author, String genre, String barcode, String coverImagePath) {
        this(title, author, genre, barcode, coverImagePath);
        this.id = id;
    }

    // Getter dan Setter untuk setiap field

    /**
     * Mendapatkan ID buku.
     * @return ID buku
     */
    public int getId() {
        return id;
    }

    /**
     * Mengatur ID buku.
     * @param id ID buku
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Mendapatkan judul buku.
     * @return Judul buku
     */
    public String getTitle() {
        return title;
    }

    /**
     * Mengatur judul buku.
     * @param title Judul buku
     */
    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    /**
     * Mendapatkan nama penulis buku.
     * @return Nama penulis buku
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Mengatur nama penulis buku.
     * @param author Nama penulis buku
     */
    public void setAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        this.author = author;
    }

    /**
     * Mendapatkan genre buku.
     * @return Genre buku
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Mengatur genre buku.
     * @param genre Genre buku
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Mendapatkan barcode buku.
     * @return Barcode buku
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Mengatur barcode buku.
     * @param barcode Barcode buku
     */
    public void setBarcode(String barcode) {
        if (barcode == null || barcode.isEmpty()) {
            throw new IllegalArgumentException("Barcode cannot be null or empty");
        }
        this.barcode = barcode;
    }

    /**
     * Mendapatkan jalur gambar sampul buku.
     * @return Jalur gambar sampul buku
     */
    public String getCoverImagePath() {
        return coverImagePath;
    }

    /**
     * Mengatur jalur gambar sampul buku.
     * @param coverImagePath Jalur gambar sampul buku
     */
    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    /**
     * Representasi objek Book dalam bentuk string untuk debugging.
     * @return String representasi objek Book
     */
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", barcode='" + barcode + '\'' +
                ", coverImagePath='" + coverImagePath + '\'' +
                '}';
    }
}
