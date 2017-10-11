package com.example.utsav.mysqldemo;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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
import java.util.HashMap;

public class nfcbgworker extends AsyncTask<String, Void, String> {
    AlertDialog alertDialog;
    Context context;
    Bundle b = new Bundle();
    Intent intent;
    HashMap<String, String> eventDetails = new HashMap<String, String>();
    public String eventid;

    nfcbgworker(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String reg_url = "http://192.168.43.231/nregister.php/";
        if (type.equals("register")) {
            try {
                eventid = params[1];

                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data =URLEncoder.encode("eventid","UTF-8")+"="+URLEncoder.encode(eventid,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
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

        eventDetails.put("eve100","App-A-thon");
        eventDetails.put("eve101","Make-A-thon");
        eventDetails.put("eve102","Hackathon");
        eventDetails.put("eve103","March Circuit");
        eventDetails.put("eve104","Breaking code");
        eventDetails.put("eve105","Battlecode");
        eventDetails.put("eve106","ACM ICPC");
        eventDetails.put("eve107","Code wars");
        eventDetails.put("eve108","Code warming");
        eventDetails.put("eve109","Code Horizon");
        if (!result.equals("failure")) {
            alertDialog.setMessage("Registered");
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle("New Event registered");
            mBuilder.setContentText("You are registered to "+eventDetails.get(eventid));
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(7, mBuilder.build());

            alertDialog.show();
        } else {
            alertDialog.setMessage(result);
            alertDialog.show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
