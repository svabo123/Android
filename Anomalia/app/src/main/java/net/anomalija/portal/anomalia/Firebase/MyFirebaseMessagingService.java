package net.anomalija.portal.anomalia.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import net.anomalija.portal.R;
import net.anomalija.portal.anomalia.Activitys.NotificationActivity;
import net.anomalija.portal.anomalia.Model.Constants;
import net.anomalija.portal.anomalia.Model.SharePref;
import java.util.HashMap;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SharedPreferences preferences = SharePref.getInstance(this, SharePref.NOTIFICATION);
        String s = preferences.getString("notification", "on");

//      setting notification
        if (s.equals("on")) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setAutoCancel(true);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            builder.setTicker("Ovo je tiker");
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setContentText(remoteMessage.getData().get("message1"));
//            builder.setContentTitle(remoteMessage.getData().get("message"));
            builder.setWhen(System.currentTimeMillis());
            Intent intent2 = new Intent(this, NotificationActivity.class);
            intent2.putExtra("id", remoteMessage.getData().get("id"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(156151, builder.build());
        }
        else {
            Log.e("NOTIFICATION", "Notification off");
        }
    }

    @Override
    public void onNewToken(String s) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token", "Refreshed token: " + token);
        registre(token);
    }
//    adding tokein into database(fcm)
    private void registre(final String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.registre,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response","Nema neta");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Token", token);


                return params;
            }
        };

        requestQueue.add(postRequest);
    }
}
