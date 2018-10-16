package net.anomalija.portal.anomalia.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

// used for getting single instance of SharePreferences and SharePreferences.Editor. Singlton patern used
public class SharePref {
    public static final String NOTIFICATION = "notification";
    public static final String TEXT_SIZE = "textSize";
    private static SharedPreferences notifications;
    private static SharedPreferences text_sizes;
    private static SharedPreferences.Editor notification_editor;
    private static SharedPreferences.Editor text_editor;
    
    //prevent creating multiple instances by making the constructor private
    private SharePref(){}

    public static SharedPreferences getInstance(Context context, String type){
        if (type.equals(TEXT_SIZE)){
            if (text_sizes == null){
               text_sizes = context.getSharedPreferences("textSize", Context.MODE_PRIVATE);
                Log.e("SharePref:", " Notification created");
               return text_sizes;
            } else return text_sizes;
            

        } else if (type.equals(NOTIFICATION)){
            if (notifications == null){
                notifications = context.getSharedPreferences("notification", Context.MODE_PRIVATE);
                return notifications;
            } else return notifications;

        }
        return null;

    }

    public static SharedPreferences.Editor getEditior(Context context, String type){

        if (type.equals(NOTIFICATION)) {
            if (notification_editor == null){
            SharedPreferences preferences = SharePref.getInstance(context, NOTIFICATION);
            notification_editor = preferences.edit();
            return notification_editor;
            }
            else return notification_editor;
        }
        if (type.equals(TEXT_SIZE)) {
            if (text_editor == null){
            SharedPreferences preferences = SharePref.getInstance(context, TEXT_SIZE);
            text_editor = preferences.edit();
            return text_editor;
            }
            else return text_editor;
        }

        return null;
    }

    
    
}
