package com.example.libraryapp_wmp2024;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Book> bookList;

    public BookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        }

        TextView bookTitleTextView = convertView.findViewById(R.id.bookTitleTextView);
        TextView bookAuthorTextView = convertView.findViewById(R.id.bookAuthorTextView);
        ImageView bookCoverImageView = convertView.findViewById(R.id.bookCoverImageView);

        Book book = bookList.get(position);
        bookTitleTextView.setText(book.getTitle());
        bookAuthorTextView.setText(book.getAuthor());

        String coverImagePath = book.getCoverImagePath();
        if (coverImagePath != null && !coverImagePath.isEmpty()) {
            Glide.with(context).load(coverImagePath).into(bookCoverImageView);
        } else {
            Glide.with(context).load(R.drawable.ic_default_cover).into(bookCoverImageView);
        }

        return convertView;
    }
}
