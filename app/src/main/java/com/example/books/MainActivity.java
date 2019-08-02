package com.example.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView thumbnail = new ImageView(this);
        thumbnail.setImageResource(R.drawable.content);
        BitmapDrawable drawable = (BitmapDrawable) thumbnail.getDrawable();
        Bitmap th = drawable.getBitmap();

        Book b1 = new Book("http1", th,"hola","jose","23/03/99","misterio",890.6,"https://play.google.com/store/books/details?id=hpMoDwAAQBAJ&rdid=book-hpMoDwAAQBAJ&rdot=1&source=gbs_api");
        Book b2 = new Book("http2", th,"ronaldo","tite","11/07/88","accion",150.6,"googleBooks.com");
        Book b3 = new Book("http3", th,"tete","maria","23/03/56","fantasia",358.1,"amazon.com");

        ArrayList<Book> books = new ArrayList<>();
        books.add(b1);
        books.add(b2);
        books.add(b3);
        books.add(b3);
        books.add(b3);
        books.add(b3);
        books.add(b3);

        ListView booksListView = findViewById(R.id.list);
        ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(this, R.color.back3));
        booksListView.setBackground(cd);
        mBookAdapter = new BookAdapter(this,books);
        booksListView.setAdapter(mBookAdapter);
    }
}
