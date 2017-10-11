package com.example.utsav.mysqldemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Register extends AppCompatActivity {
    EditText eregno,ename,esurname,eusername,epassword,ebranch,esem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ename = (EditText) findViewById(R.id.e_fname);
        esurname = (EditText) findViewById(R.id.e_lname);
        eregno = (EditText) findViewById(R.id.e_regno);
        ebranch = (EditText) findViewById(R.id.e_branch);
        eusername = (EditText) findViewById(R.id.e_uname);
        epassword = (EditText) findViewById(R.id.e_pwd);
        esem = (EditText) findViewById(R.id.e_sem);
    }

    public void OnReg(View view){
        String regno = eregno.getText().toString();
        String name =  ename.getText().toString();
        String surname = esurname.getText().toString();
        String username =  eusername.getText().toString();
        String password = epassword.getText().toString();
        String branch =  ebranch.getText().toString();
        String sem = esem.getText().toString();
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,regno,name,surname,username,password,branch,sem);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu
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
                SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                //Getting editor
                SharedPreferences.Editor editor = preferences.edit();

                //Puting the value false for loggedin
                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                //Putting blank value to email
                editor.putString(Config.EMAIL_SHARED_PREF, "");

                //Saving the sharedpreferences
                editor.commit();

                //Starting login activity
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.item3:
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
