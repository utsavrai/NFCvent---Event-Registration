package com.example.utsav.mysqldemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText usernameEt, passwordEt;
    public boolean loggedIn = false;
    public String username;
    public String password;
    public ImageButton eventbtn;
    public String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEt = (EditText) findViewById(R.id.editText);
        passwordEt = (EditText) findViewById(R.id.editText2);
        type = "login";
        eventbtn =(ImageButton)findViewById(R.id.imageButton);
        eventbtn.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    managerbr mgbr = new managerbr(MainActivity.this);
                    mgbr.execute("details");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void OnLogin(View view){
        username = usernameEt.getText().toString();
        password = passwordEt.getText().toString();

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,username,password);
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(!response.equalsIgnoreCase(Config.LOGIN_Failure)){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.EMAIL_SHARED_PREF, username);

                            //Saving values to editor
                            editor.commit();

                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_EMAIL, username);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void OpenReg(View view){
        startActivity(new Intent(this,Register.class));
    }
    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                //Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(getApplicationContext(),About.class);
                //intent.putExtra(text1,"This is text from main activity to activity 2: "+text.getText().toString());
                startActivity(intent1);
                return true;
            case R.id.item2:
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}