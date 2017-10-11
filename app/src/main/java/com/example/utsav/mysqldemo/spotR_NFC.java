package com.example.utsav.mysqldemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.TotalCaptureResult;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Utsav on 15-Apr-17.
 */


public class spotR_NFC extends AppCompatActivity  implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback {
    private ArrayList<String> messagesReceivedArray = new ArrayList<>();

    //Text boxes to add and display our messages
    private String url;
    private Document htmlDocument;
    private String htmlPageUrl;
    private String htmlContentInStringFormat, eventid;
    private TextView parsedHtmlNode;
    private Boolean buttonExists = false;
    private Button myButton;
    String reg, reg2;

    private NfcAdapter mNfcAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.spotnfc);
        Intent i = getIntent();
        reg = i.getStringExtra("reg");
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter != null) {
            mNfcAdapter.setNdefPushMessageCallback(this, this);
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        } else {
            Toast.makeText(this, "NFC not available on this device",
                    Toast.LENGTH_SHORT).show();
        }

        //txtReceivedMessages = (TextView) findViewById(R.id.txtMessagesReceived);
        updateTextViews();

        if (getIntent().getAction() != null) {
            if (getIntent().getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
                handleNfcIntent(getIntent());
        }

        parsedHtmlNode = (TextView) findViewById(R.id.html_content);
        myButton = new Button(getApplicationContext());
        myButton.setText("Register");

        LinearLayout ll = (LinearLayout) findViewById(R.id.linear_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                        nfcbgworker bWorker = new nfcbgworker(spotR_NFC.this);
                        try {
                            bWorker.execute("register", eventid);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }

    private void handleNfcIntent(Intent NfcIntent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcIntent.getAction())) {
            Parcelable[] receivedArray =
                    NfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (receivedArray != null) {
                NdefMessage receivedMessage = (NdefMessage) receivedArray[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (NdefRecord record : attachedRecords) {
                    String string = new String(record.getPayload());
                    if (string.equals(getPackageName())) {
                        continue;
                    }
                    messagesReceivedArray.add(string);


                }
                updateTextViews();
            } else {
                Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateTextViews();
        handleNfcIntent(getIntent());
    }


    private void updateTextViews() {
        if (messagesReceivedArray.size() > 0) {
            url = messagesReceivedArray.get(0);
            url = url.substring(3);
            ((TextView) findViewById(R.id.html_content)).setText(url);
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("lastMessagesReceived", messagesReceivedArray);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        messagesReceivedArray = savedInstanceState.getStringArrayList("lastMessagesReceived");
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return null;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlPageUrl = "http://" + url;
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                htmlContentInStringFormat = htmlDocument.getElementById("EventName")
                        .getElementsByTag("h2").get(0).toString();
                htmlContentInStringFormat += htmlDocument.getElementById("EventDetails")
                        .getElementsByTag("p").get(0).toString();
                eventid = htmlDocument.getElementById("eventID").text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            parsedHtmlNode.setText(Html.fromHtml(htmlContentInStringFormat));
            messagesReceivedArray.add(reg);

        }

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
                SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                //Getting editor
                SharedPreferences.Editor editor = preferences.edit();

                //Puting the value false for loggedin
                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                //Putting blank value to email
                editor.putString(Config.EMAIL_SHARED_PREF, "");

                //Saving the sharedpreferences
                editor.commit();

                //Starting login activity
                Intent intent = new Intent(spotR_NFC.this, MainActivity.class);
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
