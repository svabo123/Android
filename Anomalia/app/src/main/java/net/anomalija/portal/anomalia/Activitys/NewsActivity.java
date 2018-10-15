package net.anomalija.portal.anomalia.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.anomalija.portal.R;
import net.anomalija.portal.anomalia.Model.Category;
import net.anomalija.portal.anomalia.Model.Constants;
import net.anomalija.portal.anomalia.Model.News;
import net.anomalija.portal.anomalia.Model.SharePref;

import java.io.Serializable;

public class NewsActivity extends AppCompatActivity implements Serializable{

    private static final String TAG = "NewsActivity ";
    
    private TextView sadrzaj;
    private TextView autor;
    private TextView datum;
    private TextView naslov;
    private ImageView imageView;
    private TextView kategorija;
    private String guid;
    private static double text_size;
    private String id;
    private News news  = null;
    private Category category;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private  Intent extras;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent();
        if (isConnected()){
        setContentView(R.layout.activity_news2);

            initialization();
//            getting category for post
            category = (Category) extras.getSerializableExtra("category");

//        setting content for activitiy
            setContent();  }
            else setContentView(R.layout.no_internet);

    }
    private void setContent(){

        extras = getIntent();
//        get base News- fewer attributes
        news = (News) extras.getSerializableExtra("news");
        id = String.valueOf(news.getId());
        editor = SharePref.getEditior(this, SharePref.TEXT_SIZE);
        text_size = preferences.getFloat("size", 20);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
//        setting content for news
        String urlString = Constants.gettingContent + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setValues(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        this line send error proboble because internet connection
                        Toast.makeText(getApplicationContext(), "Greska u konekciji", Toast.LENGTH_LONG).show();

                    }
                });

        //add request to queue
        requestQueue.add(stringRequest);
//        getting category
        String urlCategory = Constants.gettingCategoryFromPost + id;
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, urlCategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String kategorija_text = String.valueOf(Html.fromHtml(response));
                        kategorija.setText(kategorija_text);
                        getSupportActionBar().setTitle(Html.fromHtml("<font color='#112'>" + kategorija_text + "</font>"));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERRROR", error.getMessage().toString());
                    }
                });
        //add request to queue
        requestQueue.add(stringRequest1);

        naslov.setText(news.getNaslov());

        //        postavljanje slike



        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        RequestOptions requestOptions1 = new RequestOptions();
        requestOptions1.override(1200, 800);
        imageView = findViewById(R.id.slika);
        Glide.with(this)
                .asBitmap()
                .apply(requestOptions)
                .load(news.getSlika())
//                .apply(requestOptions1)
                .into(imageView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.news_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.share:
//                setting share option on social network
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, guid);
                startActivity(Intent.createChooser(intent, "Podijeli"));
                return true;
            case R.id.text_size:
//                setting text size in news activity
                setTextSize();
        }
        return super.onOptionsItemSelected(item);
    }


//    setting text size for text_size
    private void setTextSize() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] niz = {"Mala", "Srednja", "Velika"};
        builder.setTitle("Velicina teksta");
        int checkedItem = 0;
        Log.e("TextSize", String.valueOf(text_size));
        switch ((int) text_size) {
            case 20:
                checkedItem = 0;
                break;
            case 24:
                checkedItem = 1;
                break;
            case 30:
                checkedItem = 2;
                break;

        }
        builder.setSingleChoiceItems(niz, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (niz[which]) {
                    case "Mala":
                        sadrzaj.setTextSize(20);
                        editor.putFloat("size", 20);
                        editor.commit();
                        break;
                    case "Srednja":
                        sadrzaj.setTextSize(24);
                        editor.putFloat("size", 24);
                        editor.commit();
                        break;
                    case "Velika":
                        sadrzaj.setTextSize(30);
                        editor.putFloat("size", 30);
                        editor.commit();
                        break;

                }
            }
        });
        builder.show();
    }

    private void initialization() {
        kategorija = findViewById(R.id.kategorija);
        sadrzaj = findViewById(R.id.sadrzaj);
        autor = findViewById(R.id.autor);
        datum = findViewById(R.id.datum);
        imageView = findViewById(R.id.slika);
        naslov = findViewById(R.id.naslov);
        preferences = SharePref.getInstance(this, SharePref.TEXT_SIZE);
    }

    private void setValues(String response){
        String content = response.split("!!!!!")[0];
        String autor_text = response.split("!!!!!")[1];
        String datum_text = response.split("!!!!!")[2];
        guid = response.split("!!!!!")[3];
//                        setting content
        content = String.valueOf(Html.fromHtml(content));
        content = content.replace("[headline]", "");
        content = content.replace("[/headline]", "");
        sadrzaj.setText(content);
        text_size = preferences.getFloat("size", 20);
        sadrzaj.setTextSize((float) text_size);
//                       set autor
        autor.setText("Autor teksta: " + autor_text);
//                       set time

        datum.setText(datum_text.split(" ")[0]);


    }

    @Override
    public void onBackPressed() {
        
        Intent intent = new Intent(NewsActivity.this, MainActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
        super.onBackPressed();

    }


//    fecthing content from server

//    checking internet status
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }
    public void relode(View view){
        onRestart();
    }

    //    called from metohod refresh, restarting app

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(NewsActivity.this, NewsActivity.class);
        i.putExtra("news",extras.getSerializableExtra("news"));
        i.putExtra("category", extras.getSerializableExtra("category"));
        startActivity(i);
        finish();

    }


}

