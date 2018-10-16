package net.anomalija.portal.anomalia.Activitys;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.anomalija.portal.R;
import net.anomalija.portal.anomalia.Model.Category;
import net.anomalija.portal.anomalia.Model.Constants;
import net.anomalija.portal.anomalia.Model.News;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";
    private String id;
    private News news;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        intent = getIntent();
        id = intent.getStringExtra("id");

        if (isConnected()) {
            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.gettingNewsFromId + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals(Constants.ERROR)) {
                        News news = getClearText(response);
                        Intent intent1 = new Intent(NotificationActivity.this, NewsActivity.class);
                        intent1.putExtra("news", news);
                        getApplicationContext().startActivity(intent1);

                    } else
                        Toast.makeText(getApplicationContext(), "GREŠKA NA SERVERU", Toast.LENGTH_LONG).show();
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Log.e("ERRROR", error.getMessage().toString().equals("") ? error.getMessage().toString() : "Greska u ucitavanju");
                            Toast.makeText(getApplicationContext(), "GREŠKA U KONEKCIJI", Toast.LENGTH_LONG).show();
                            setContentView(R.layout.no_internet);

                        }
                    });
            //add request to queue
            requestQueue.add(stringRequest);

        }
        else setContentView(R.layout.no_internet);

    }


    public void relode(View view){
        onRestart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(NotificationActivity.this, NotificationActivity.class);
        i.putExtra("id", intent.getStringExtra("id"));
        startActivity(i);
        finish();
    }

    //      checking network status
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }


    private News getClearText (String response){
        response = String.valueOf(Html.fromHtml(response));
        String start[] = response.split("!!!!!!!!!!!!");

        for (int i = 0; i < start.length; i++) {
            try {
//              getting content for NewsActivity
                String datum = start[i].split("/////")[1];
                String sadrzaj = start[i].split("/////")[2];
                int id = Integer.valueOf(sadrzaj);
                String naslov = start[i].split("/////")[3];
                String slika = start[i].split("/////")[4];
                news = new News(id, naslov, "", "Autor", slika, datum);
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }
        return news;
    }

}
