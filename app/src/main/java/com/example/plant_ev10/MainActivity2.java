package com.example.plant_ev10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {

    private final String DEFAULT_HOST = "plante.sytes.net/";
    private final Integer DEFAULT_PORT = 80;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable yourTaskRunner = new Runnable() {
            public void run() {
                getPlantResponse(DEFAULT_HOST, DEFAULT_PORT);
            }
        };
        scheduler.scheduleAtFixedRate(yourTaskRunner, 20, 20, TimeUnit.SECONDS);
    }

    private void getPlantResponse(String host, int port) {

        String url = String.format("http://%s:%s", host, port);
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callBack(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(getWindow().getDecorView().getRootView(), "Errore nella ricezione dati dai sensori", Snackbar.LENGTH_LONG)
                                    .setAnchorView((R.id.snackBarLay))
                                    .setAction("Action", null).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Errore nella ricezione dati dai sensori", Snackbar.LENGTH_LONG)
                                .setAnchorView(R.id.snackBarLay)
                                .setAction("Action", null).show();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);

    }

    private void callBack(JSONObject response) throws JSONException {

        String temperatura = response.isNull("temperatura") ? null : response.get("temperatura").toString(), umidita = response.isNull("umidita") ? null : response.get("umidita").toString(), livelloAcqua = response.isNull("livelloAcqua") ? null : response.get("livelloAcqua").toString(), luce = response.isNull("luce") ? null : response.get("luce").toString();

        TextView tempTextView = findViewById(R.id.temptTest);
        tempTextView.setText(String.valueOf(temperatura + " °"));

        TextView luxTextView = findViewById(R.id.luxTest);
        luxTextView.setText(String.valueOf(luce));

        TextView lvlAcqua = findViewById(R.id.acquatest);
        lvlAcqua.setText(String.valueOf(livelloAcqua));

        TextView lvlUmidity = findViewById(R.id.testUmidity);
        lvlUmidity.setText(String.valueOf(umidita + " %"));

        if (livelloAcqua == null || livelloAcqua.equalsIgnoreCase("nan") || Double.valueOf(livelloAcqua) > 70) {
            sendNotification("SVUOTARE ACQUA!!");
        }
        if (umidita == null || umidita.equalsIgnoreCase("nan") || Double.valueOf(umidita) < 25){
                sendNotification("UMIDITA' TERRENO BASSA");
        }
    }

    public void sendNotification(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("canale", "canale", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity2.this, "canale");
        builder.setContentTitle("PLANT-E -WARNING-");
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.sealevel);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity2.this);
        managerCompat.notify(1, builder.build());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        HomeActivity.mp.stop();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        HomeActivity.mp.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        HomeActivity.mp.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        HomeActivity.mp.start();
    }
}