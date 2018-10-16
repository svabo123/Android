package net.anomalija.portal.anomalia.Activitys;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.anomalija.portal.R;


public class WelcomeActiivity extends AppCompatActivity {

//    use to make splash screen
private static final String TAG = "WelcomeActiivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
         Thread thread = new Thread(new Runnable() {

             @Override
             public void run() {
                 try {
//                     set time of splash screen
                     Thread.sleep(2000);
                     Intent welcomeIntent = new Intent(WelcomeActiivity.this, MainActivity.class);
                     startActivity(welcomeIntent);
                     finish();
                 } catch (InterruptedException e) {
                    Log.e (TAG, e.getMessage());
                 }
             }
         });
         thread.start();
    }
}
