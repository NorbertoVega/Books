package com.example.books;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter( Context context, List<Book> books) {
        super(context,0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final Book currentBook = getItem(position);

        ImageView thumbnail = listItemView.findViewById(R.id.thumbnail_t_v);
        String url = intoString(currentBook.getmImageLink(),"s",4);

        Log.d("BookAdapter: ",url);
        Glide.with(getContext()).load(currentBook.getmImageLink()).into(thumbnail);

        TextView title = listItemView.findViewById(R.id.title_text_view);
        TextView author = listItemView.findViewById(R.id.author_text_view);
        TextView date = listItemView.findViewById(R.id.date_published_text_view);
        TextView category = listItemView.findViewById(R.id.category_text_view);
        TextView price = listItemView.findViewById(R.id.price_text_view);

        title.setText(currentBook.getmTitle());
        author.setText("Author: " + currentBook.getmAuthor());
        date.setText("Published: "+ currentBook.getmPublishedDate());
        category.setText("Category: " + currentBook.getmCategory());
        if(currentBook.getmPrice() == 0) {
            price.setText("Not for sale.");
        }
        else
            price.setText("Price: $" + currentBook.getmPrice());

        Button buyButton = listItemView.findViewById(R.id.buy_button);
        if(currentBook.getmBuyLink().equals("")) {
            buyButton.setText("Not available");
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"Not available for sale.",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            buyButton.setText("Buy!!");
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getmBuyLink()));
                    if (browserIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        getContext().startActivity(browserIntent);
                    }
                }
            });
        }
        return listItemView;
    }

    private String intoString(String textoReal, String textoInsert, int pos){
        StringBuilder stringBuilder= new StringBuilder(textoReal);
        stringBuilder.insert(pos,textoInsert);
        return stringBuilder.toString();
    }
}
