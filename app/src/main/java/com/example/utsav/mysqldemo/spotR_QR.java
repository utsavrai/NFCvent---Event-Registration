package com.example.utsav.mysqldemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class spotR_QR extends AppCompatActivity{

    private Document htmlDocument;
    private String htmlPageUrl;
    private TextView parsedHtmlNode;
    private String htmlContentInStringFormat;
    private IntentIntegrator qrScan;
    private String url, eventid;
    String reg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.spotqr);
        Intent i = getIntent();
         reg = i.getStringExtra("reg");

        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();

        parsedHtmlNode = (TextView) findViewById(R.id.html_content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                url = result.getContents().toString();
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlPageUrl = url;
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                htmlContentInStringFormat = htmlDocument.getElementById("EventName")
                        .getElementsByTag("h2").get(0).toString();
                htmlContentInStringFormat += htmlDocument.getElementById("EventDetails")
                        .getElementsByTag("p").get(0).toString();
                eventid = htmlDocument.getElementById("eventID").text().toLowerCase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            parsedHtmlNode.setText(Html.fromHtml(htmlContentInStringFormat));
            Toast.makeText(getApplicationContext(), eventid, Toast.LENGTH_SHORT).show();

        }
    }

    public void register(View v){
        qrbgworker qbWorker = new qrbgworker(this);
        qbWorker.execute("register", reg, eventid);
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
                Intent intent = new Intent(spotR_QR.this, MainActivity.class);
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
