package com.example.eunicechu.trippie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (this.getIntent().getExtras() != null)
        {
            Toast.makeText(this, this.getIntent().getExtras().getString("value"), Toast.LENGTH_LONG).show();
        }
    }
    public void activityMap (View v)
    {
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("buttonID", v.getId());
        startActivity(i);
    }

}
