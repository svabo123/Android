package net.anomalija.portal.anomalia.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import net.anomalija.portal.R;


public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#111'>Anomalija</font>"));

    }
}
