package net.anomalija.portal.anomalia.Activitys;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.anomalija.portal.R;
import net.anomalija.portal.anomalia.Model.SharePref;


public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Switch aSwitch;
    private SharedPreferences prefNotification;
    private SharedPreferences prefTextSize;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editorNotification;
    private Spinner spinner;
    private TextView version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initialization();
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#111'>Anomalija</font>"));

        setVersionTextView();

        String s = prefNotification.getString("notification", "on");
        float text_size = prefTextSize.getFloat("size", 20);
        setSpinnerText((int) text_size);



        if (s.equals("off"))  {
            aSwitch.setChecked(false);
        }
        else if (s.equals("on")){
            aSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              Log.e("IsChecked", String.valueOf(isChecked));
              if (isChecked){
                  editorNotification.putString("notification", "on");
                  editorNotification.commit();
                  Toast.makeText(getApplicationContext(), "Obavjestenja su ukljucena", Toast.LENGTH_SHORT).show();
              } else {
                  editorNotification.putString("notification", "off");
                  editorNotification.commit();
                  Toast.makeText(getApplicationContext(), "Obavjestenja su iskljucena", Toast.LENGTH_SHORT).show();

              }




            }
        });

    }
//     setting text in version TextView
    private void setVersionTextView() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionText = pInfo.versionName;
            version.setText(versionText);
            Log.e("VERSION", versionText);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version.setText("nepoznata");
        }
    }


    private void setSpinnerText(int f) {
        switch (f){
            case 20: spinner.setSelection(0); break;
            case 24: spinner.setSelection(1); break;
            case 30: spinner.setSelection(2); break;

        }
    }

//     handling click on spiner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
                    case 0: editor.putFloat("size", 20);
                            editor.apply();
                            setSpinnerText(20);
                            break;
                    case 1: editor.putFloat("size", 24);
                            editor.apply();
                            setSpinnerText(24);
                            break;
                    case 2: editor.putFloat("size", 30);
                            editor.apply();
                            setSpinnerText(30);
                            break;
                }
            }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


    private void initialization() {
        version = findViewById(R.id.verison);
        aSwitch = findViewById(R.id.switch1);
        prefNotification = SharePref.getInstance(this, SharePref.NOTIFICATION);
        prefTextSize = SharePref.getInstance(this, SharePref.TEXT_SIZE);
        editor = prefTextSize.edit();
        editorNotification = prefNotification.edit();
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.velicine_teksta, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent( SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

