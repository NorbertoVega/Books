package com.example.books;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;
    private Context mContext;

    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<Book> loadInBackground() {
        if(mUrl == null)
            return null;
        List<Book> books = QueryUtils.fetchBookData(mUrl, mContext);
        return books;
    }
}
