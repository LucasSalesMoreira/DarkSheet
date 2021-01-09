package com.sales.darksheet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sales.darksheet.base.Data;
import com.sales.darksheet.service.MyJobIntentService;
import com.sales.darksheet.service.NotificationService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.emitter.Emitter;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_search, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Data.SOCKET.on("NEW_MSG_IN_HOME", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject messageObject = new JSONObject(String.valueOf(args[0]));
                            System.out.println("Mensagem recebida: " + messageObject);
                            Toast.makeText(getApplicationContext(), "NA HOME: " + messageObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            MyJobIntentService.startJobService(getApplicationContext(), new Intent(), 0);
        else
            startService(new Intent(getApplicationContext(), NotificationService.class));
    }
}