package net.anomalija.portal.anomalia.Activitys;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.anomalija.portal.R;


public class WelcomeActiivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
         Thread thread = new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     Thread.sleep(2000);
                     Intent welcomeIntent = new Intent(WelcomeActiivity.this, MainActivity.class);
                     startActivity(welcomeIntent);
                     finish();

                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         });
         thread.start();
    }
}
