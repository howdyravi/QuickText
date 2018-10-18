package com.example.howdyravi.quicktext;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS
    };
    TextInputEditText msgEtx;
    ImageView saveBtn;
    SharedPreferences sharedPreferences ,sharedPreference3;
    Switch aSwitch;
    ListView statusListView;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        msgEtx=findViewById(R.id.msg_etx);
        saveBtn =findViewById(R.id.save_btn);
        aSwitch=findViewById(R.id.switchEnable);
        statusListView = findViewById(R.id.statusListView);
        sharedPreferences=this.getSharedPreferences("msgFileKey",Context.MODE_PRIVATE);
        sharedPreference3 = getSharedPreferences("Edittext",Context.MODE_PRIVATE);




        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));


        final String[] statusListItems = {"With Bae", "Do not disturb", "At Vacations","Busy Today","Driving","In Shower","If urgent text me"};

        arrayAdapter = new ArrayAdapter(this, R.layout.status_list_items_series, R.id.option, statusListItems);
        statusListView.setAdapter(arrayAdapter);

        statusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                msgEtx.setText(statusListItems[i]);

            }
        });



        SharedPreferences sharedPreferences2 = this.getSharedPreferences("msgFileKey",Context.MODE_PRIVATE);
        String service = sharedPreferences2.getString("service","0");
        if(service.equals("on"))
            aSwitch.setChecked(true);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {

                    Toast.makeText(MainActivity.this, "Activated", Toast.LENGTH_SHORT).show();
                    aSwitch.setText("ON");
                    Log.d("Toggle","ON");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("service","on");
                    editor.commit();
                    Log.d("Service pref","ON");


                }
                else
                {
                    Toast.makeText(MainActivity.this, "Deactivated", Toast.LENGTH_SHORT).show();
                    aSwitch.setText("OFF");
                    Log.d("Toggle","OFF");
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("service","off");
                    editor.commit();
                    Log.d("Service pref","OFF");

                }
            }
        });


    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        String message = sharedPreference3.getString("edittext", "Can't talk now.");
        msgEtx.setText(message);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = msgEtx.getText().toString();

                SharedPreferences.Editor editor2 = sharedPreference3.edit();
                editor2.putString("edittext", message);
                editor2.commit();

                Toast.makeText(MainActivity.this, "Message Saved Successfully", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                String QuickText = getResources().getString(R.string.urltext);
                editor.putString("msgKey",msgEtx.getText().toString()+"\n" + "\n"+"Message Forwarded By :"+"\n"+ QuickText + "\n" +"https://bit.ly/2A66GGc");
                editor.commit();

            }
        });
    }
}
