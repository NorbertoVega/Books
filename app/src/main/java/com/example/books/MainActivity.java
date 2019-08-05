package com.example.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOK_LOADER_ID = 1;
    private String book_request_url;
    private static final String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    BookAdapter mBookAdapter;
    private TextView emptyStateTextView;
    private ProgressBar mProgressBar;
    private String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        ListView booksListView = findViewById(R.id.list);
        ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(this, R.color.back3));
        booksListView.setBackground(cd);
        mBookAdapter = new BookAdapter(this,new ArrayList<Book>());
        booksListView.setAdapter(mBookAdapter);

        emptyStateTextView = findViewById(R.id.empty_view);
        booksListView.setEmptyView(emptyStateTextView);

        Button searchButton = findViewById(R.id.search_button);
        final EditText searchText = findViewById(R.id.edit_text_view);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = searchText.getText().toString();
                if(!input.equals("")) {
                    book_request_url = formHttpReq(input);
                    startProcess();
                }else {
                    emptyStateTextView.setText(R.string.no_books);
                    mBookAdapter.clear();
                }
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    public void startProcess(){
        mBookAdapter.clear();
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            getLoaderManager().restartLoader(BOOK_LOADER_ID,null,this);
        }else {
            mProgressBar = findViewById(R.id.progress_bar);
            mProgressBar.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, book_request_url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        if(data == null)
            emptyStateTextView.setText(R.string.no_books);

        if(data != null && !data.isEmpty()){
            mBookAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookAdapter.clear();
        Log.d("onloadReset: ", "reseteado");
    }

    private String formHttpReq(String input){
        String httpReq = BOOK_REQUEST_URL;
        int pos;
        if(input.charAt(input.length()-1) == ' '){
            input = input.substring(0,input.length()-1);
        }
        String inicio, fin;
        while(input.contains(" ")) {
            pos = input.indexOf(" ");
            inicio = input.substring(0,pos);
            fin = input.substring(pos+1);
            input = inicio + "+" + fin;
        }

        httpReq += input;
        httpReq += "&maxResults=25";
        return httpReq;
    }

}
