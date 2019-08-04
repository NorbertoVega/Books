package com.example.books;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<Book> fetchBookData(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        List<Book> books = extractBooks(jsonResponse);
        return books;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error creating url ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG,"problem retrieving the book JSON results");
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
            if(inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader= new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<Book> extractBooks(String jsonResponse){
        ArrayList<Book> books = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray itemsArray = root.getJSONArray("items");

            for (int i = 0; i<itemsArray.length(); i++){
                JSONObject obj = (JSONObject) itemsArray.get(i);
                JSONObject volumeInfo = obj.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                String authorString = "";
                JSONArray authors = null;
                try {
                    authors =  volumeInfo.getJSONArray("authors");
                    for (int j = 0; j<authors.length(); j++){
                        authorString += authors.get(j) + " " ;
                    }
                } catch (JSONException e){
                    authorString = "unknown";
                }
                
                String categoriesString = "";
                JSONArray categories = null;
                try{
                    categories =  volumeInfo.getJSONArray("categories");
                    for (int k = 0; categories != null &&k<categories.length(); k++){
                        categoriesString += categories.get(k) + " ";
                    }
                }catch (JSONException e){
                    categoriesString = "there is not information";
                }

                String date = "";
                try {
                    date = volumeInfo.getString("publishedDate");
                }catch (JSONException e){
                    date = "unknown";
                }

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String imLink = imageLinks.getString("smallThumbnail");

                Bitmap image = getBitmapFromUrl(imLink);

                JSONObject saleInfo = obj.getJSONObject("saleInfo");
                String saleability = saleInfo.getString("saleability");
                double price;
                String buyLink = "";
                if(saleability.equals("NOT_FOR_SALE")){
                    price = 0;
                }else{
                    JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                    price = listPrice.getDouble("amount");
                    buyLink = saleInfo.getString("buyLink");
                }

                Book currentBook = new Book(imLink, image, title, authorString, date, categoriesString, price, buyLink);
                books.add(currentBook);
            }
        } catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }

    public static Bitmap getBitmapFromUrl(String url) {
        String urldisplay = url;
        Bitmap bmp = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bmp;
    }
}
