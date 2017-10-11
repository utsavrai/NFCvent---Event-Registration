package com.example.utsav.mysqldemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Utsav Rai on 02-Feb-17.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {
    AlertDialog alertDialog;
    Context context;
    Bundle b = new Bundle();
    Intent intent;
    BackgroundWorker (Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://192.168.43.231/login.php/";
        String reg_url = "http://192.168.43.231/register.php/";
        if(type.equals("login")){
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line;
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(type.equals("register")){
            try {
                String regno = params[1];
                String name = params[2];
                String surname = params[3];
                String username = params[4];
                String password = params[5];
                String branch = params[6];
                String sem = params[7];

                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("regno","UTF-8")+"="+URLEncoder.encode(regno,"UTF-8")+"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8") + "&" +
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8") + "&" +
                        URLEncoder.encode("surname","UTF-8")+"="+URLEncoder.encode(surname,"UTF-8")+"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8") + "&" +
                        URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8") + "&" +
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8") + "&" +
                        URLEncoder.encode("branch","UTF-8")+"="+URLEncoder.encode(branch,"UTF-8")+"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8") + "&" +

                        URLEncoder.encode("sem","UTF-8")+"="+URLEncoder.encode(sem,"UTF-8")+"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line;
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Status");
    }

    @Override
    protected void onPostExecute(String result) {


        if(!result.equals("failure") && !result.equals("Registered")) {
            intent = new Intent(context, NfcActivity.class);
            b.putString("data", result);
            intent.putExtras(b);
            context.startActivity(intent);
        }
        else if(result.equals("Registered")){
            alertDialog.setMessage(result);
            alertDialog.show();
        }
        else{
            alertDialog.setMessage(result);
            alertDialog.show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }



}
