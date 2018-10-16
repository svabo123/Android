package net.anomalija.portal.anomalia.Activitys;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ProgressBar;
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
import net.anomalija.portal.anomalia.Model.RecyclerViewAdapter;
import net.anomalija.portal.anomalia.Model.SharePref;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    declaring vars
    ArrayList<News> news = null;
    ArrayList<Category> categories = null;
    SharedPreferences preferences = null;
    MenuItem item = null;
    ProgressBar prgreessBar = null;
    private  ActionBarDrawerToggle toggle;
    private DrawerLayout drawer = null;
    public static final int MAIN = 1;
    public static final int CATEGORY = 2;
    public static int type = 0;
    public static int currentVisiblePosition = 0;
    private Category category;
    private RecyclerView recyclerView  = null;
    private LinearLayoutManager layoutManager = null;
    private RecyclerViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

//       setting type of activity
        setTypeOfActivity();

//      setting var
        initialization();

//        checking internet connection
        if (isConnected()) {
            //      loading data for UI
            loadData(type);

            //      setting parametars for drawerLayout 
            displayMenu();

        } else {

//      if there is no internet, set no_internet activitiy
            setContentView(R.layout.no_internet);
        }
    }


//       setting parametars for drawerMenu, and adding category
    private void displayMenu() {
        if (type == MAIN)
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#112'>Anomalija</font>"));
        else  getSupportActionBar().setTitle(Html.fromHtml("<font color='#112'>" + category.getName()+ "</font>"));
        getSupportActionBar().setIcon(R.drawable.loading_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true)    ;
        final NavigationView navigationView = findViewById(R.id.menu_navigation);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, Constants.gettingCategory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(Constants.ERROR)) {
                categories = new ArrayList<>();
                response = String.valueOf(Html.fromHtml(response));

                String start[] = response.split("!!!!!!");
                for (int i = 0; i < start.length; i++) {
                    String name = start[i].split("!!!!!")[0];
                    int id = Integer.parseInt(start[i].split("!!!!!")[1]);
                    categories.add(new Category(id, name));
                }
                final Menu menu = navigationView.getMenu();
                SubMenu subMenu = menu.addSubMenu("Kategorije:");


                for (int i = 0; i < categories.size(); i++) {

                    final int finalI = i;
                    subMenu.add(categories.get(i).getName()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.putExtra("category", categories.get(finalI));
                            startActivity(intent);
                            return false;
                        }
                    });
                }
                    drawer.closeDrawers();
                }
                else Log.e("ERROR", " GRESKA NA SERVERU");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest1);


    }

    //    update UI
    private void refresh(ArrayList<News> list) {
        news = list;
        if (adapter == null)
            adapter = new RecyclerViewAdapter(this, list,category);

        if (recyclerView == null)
            recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
       
        if (layoutManager == null)
            layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        (recyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        prgreessBar.setVisibility(View.GONE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.example_menu, menu);
        item = menu.findItem(R.id.notification);
        String s = preferences.getString("notification", "on");
        if (s.equals("off")) {
            item.setIcon(R.drawable.ic_notifications_off_black_24dp);
        } else if (s.equals("on")) {
            item.setIcon(R.drawable.ic_notifications_black_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_us:
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent2);
                return true;
            case R.id.notification:
               setNotification(item);
                return true;
            case android.R.id.home:
                if (!drawer.isDrawerOpen(GravityCompat.START))
                    drawer.openDrawer(GravityCompat.START);
                else
                    drawer.closeDrawers();
                return true;


  
                

        }
        return super.onOptionsItemSelected(item);
    }

//    setting notificaton and set icon to notification
    private void setNotification(MenuItem item) {

        SharedPreferences.Editor editor = SharePref.getEditior(this, SharePref.NOTIFICATION);
        String s = preferences.getString("notification", "on");
        Log.e("NOTIFICATION", s);

        if (s.equals("off")) {
            editor.putString("notification", "on");
            item.setIcon(R.drawable.ic_notifications_black_24dp);
            editor.commit();
            Toast.makeText(this, "Obavještenja su uključena", Toast.LENGTH_SHORT).show();

        } else if (s.equals("on")) { editor.putString("notification", "off");
            editor.commit();
            item.setIcon(R.drawable.ic_notifications_off_black_24dp);
            Toast.makeText(this, "Obavještenja su isključena", Toast.LENGTH_SHORT).show();
        }

    }

//    handling click on social network menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home :
               return startHomeIntent();
            case R.id.facebook:
                return starIntent("https://www.facebook.com/search/top/?q=anomalija.net");
            case R.id.instagram:
                return starIntent("https://www.instagram.com/anomalijaa/");
            case R.id.anomalija:
                return starIntent("http://www.anomalija.net/");
        }
        return false;
    }

//    handling click on social network in menu
    private boolean starIntent(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        return true;
    }

//    open MainActivity, set homepage
    private boolean startHomeIntent(){
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
    }

//      checking network status
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }

//    refreshing UI, and try to connect on internet(handling button click on refresh icon)
    public void relode(View view){
        onRestart();
    }
    
//    called from metohod refresh, restarting app
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }

//    setting variables
    private void initialization() {
        preferences = getApplicationContext().getSharedPreferences("notification", Context.MODE_PRIVATE);
        categories = new ArrayList<>();
        prgreessBar = findViewById(R.id.progress_bar);

    }

//    clearing text from response
    private ArrayList<News> getClearText(String response){
    response = String.valueOf(Html.fromHtml(response));
        String start[] = response.split("!!!!!!!!!!!!");
        ArrayList<News> list = new ArrayList<>();
        for (int i = 0; i < start.length; i++) {
            try {
//              getting content for NewsActivity
                String datum = start[i].split("/////")[1];
                String sadrzaj = start[i].split("/////")[2];
                int id = Integer.valueOf(sadrzaj);
                String naslov = start[i].split("/////")[3];
                String slika = start[i].split("/////")[4];
                list.add(new News(id, naslov, "", "Autor", slika, datum));
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }
        return list;
    }

//    getting category from response
    private void getCategory(String response){
        if (!response.equals(Constants.ERROR)) {
            String start[] = response.split("!!!!!!");
            for (int i = 0; i < start.length; i++) {
                String name = start[i].split("!!!!!")[0];
                int id = Integer.parseInt(start[i].split("!!!!!")[1]);
                categories.add(new Category(id, name));
            }
        }
        else Toast.makeText(getApplicationContext(), "GREŠKA NA SERVERU", Toast.LENGTH_LONG).show();



    }
    
//    fetching data for content
    private void loadData(int appType) {
        news = new ArrayList<>();
        if (appType == MAIN) {

            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.getNews, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals(Constants.ERROR)) {
                        ArrayList<News> list = getClearText(response);
                        refresh(list);

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

            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, Constants.gettingCategory, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    getCategory(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(stringRequest1);

        } else if (appType == CATEGORY) {
            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, (Constants.gettingPostsForCategory + category.getCategory_id()),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("Connected faild")) {
                                ArrayList<News> list = getClearText(response);
                                refresh(list);
//                                updating UI
                                if (list.size() > 0) refresh(list);
                            } else
                                Toast.makeText(getApplicationContext(), "GREŠKA NA SERVERU", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERRROR", error.toString());
                            Toast.makeText(getApplicationContext(), "GREŠKA U KONEKCIJI", Toast.LENGTH_LONG).show();

                        }
                    });
            //add request to queue
            requestQueue.add(stringRequest);
//            adding on end of loading image listner
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, Constants.gettingCategory, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String start[] = response.split("!!!!!!");
                    for (int i = 0; i < start.length; i++) {
                        String name = start[i].split("!!!!!")[0];
                        int id = Integer.parseInt(start[i].split("!!!!!")[1]);
                        categories.add(new Category(id, name));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(stringRequest1);
            

        }
    }

//    getting type of activity, and setting var type
    private void setTypeOfActivity(){
        try {
            Intent extras = getIntent();
            category = (Category) extras.getSerializableExtra("category");
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#111'>" + category.getName() + "</font>"));
            type = CATEGORY;
        }catch (NullPointerException e){
            type = MAIN;
            category = null;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
        currentVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }  catch (NullPointerException exception) {
        Log.e("MainActivity", exception.toString());
        }
    }

}

