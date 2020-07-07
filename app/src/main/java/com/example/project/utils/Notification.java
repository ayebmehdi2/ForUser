package com.example.project.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Notification {

    final private String serverKey = "key=" + "AAAASMdDClk:APA91bHRPKqcTHl_wGupKe_XgAQJBsqGE5ocsc8YpuXtgu25e5Y77R3W74fnXGsy4_yxWaV7O5qz-TBfcqyWW9jjDaLu03kqBODANJy2WLjjkwMzOZfqK838qhJYVenWQeuNRmMSCIFX";
    final private String contentType = "application/json";
    private final String TAG = "NOTIFICATION TAG";


    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_MESSAGE;
    private String TOPIC;


    private Context context;
    public Notification(Context context){
        this.context = context;
    }


    public void notif(String userId, String title, String message){
        TOPIC = "/topics/" + userId; //topic must match with what the receiver subscribed to
        NOTIFICATION_TITLE = title;
        NOTIFICATION_MESSAGE = message;


        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> Log.i(TAG, "onResponse: " + response.toString()),
                error -> {
                    Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}
