package com.example.utsav.mysqldemo;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.HashMap;

/**
 * Created by Utsav Rai on 26-Mar-17.
 */

public class NfcActivity extends AppCompatActivity  implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback{

    //The array lists to hold our messages
    private String messagesToSend;
    private String messagesReceived;

    //Text boxes to add and display our messages
    private TextView txtReceivedMessages;
    private TextView txtMessagesToSend;

    private NfcAdapter mNfcAdapter;
    String[] info;
    HashMap<String,String> eventDetails;
    HashMap<String,String> eventDate;
    String[] regEvent;
    ListView listView;
    FloatingActionButton menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        eventDetails = new HashMap<String, String>();
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
        eventDate = new HashMap<String, String>();
        eventDate.put("eve100","2017-05-10");
        eventDate.put("eve101","2017-05-11");
        eventDate.put("eve102","2017-05-12");
        eventDate.put("eve103","2017-05-13");
        eventDate.put("eve104","2017-05-14");
        eventDate.put("eve105","2017-05-15");
        eventDate.put("eve106","2017-05-16");
        eventDate.put("eve107","2017-05-17");
        eventDate.put("eve108","2017-05-18");
        eventDate.put("eve109","2017-05-19");

        TextView regno = (TextView) findViewById(R.id.regno);
        TextView fname = (TextView) findViewById(R.id.fname);
        TextView lname = (TextView) findViewById(R.id.lname);
        TextView branch = (TextView) findViewById(R.id.branch);
        TextView sem = (TextView) findViewById(R.id.sem);
       // TextView status = (TextView) findViewById(R.id.statusmsg);
        Intent intent = getIntent();
        listView = (ListView) findViewById(R.id.listView);
        Bundle b = intent.getExtras();
        if (b != null) {
            String s = b.getString("data");
            info = s.split(" ");
            regno.setText(info[0]);
            fname.setText(info[1]);
            lname.setText(info[2]);
            branch.setText(info[3]);
            sem.setText(info[4]);
            int x =0;
            String str = "";
            String noti = "";
            regEvent = new String[info.length-5];
            for(int i = 5; i<info.length;i++){
                regEvent[x] = eventDetails.get(info[i]);
                x++;
                str += eventDetails.get(info[i])+" ";
                noti += "Event: " + eventDetails.get(info[i])+" on: "+eventDate.get(info[i])+"\n";
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NfcActivity.this);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle("Upcoming Events");
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(noti));
            mBuilder.setContentText(noti).build();
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(7, mBuilder.build());
            //Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, regEvent);
            //set adapter to list view//
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Toast.makeText(getApplicationContext(),
                            "Position :"+position+"  ListItem : " +regEvent[position] , Toast.LENGTH_LONG)
                            .show();
                    String product = ((TextView) view).getText().toString();

                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(getApplicationContext(), singleEvent.class);
                    // sending data to new activity
                    i.putExtra("event", info[position+5]);
                    i.putExtra("reg",info[0]);
                    i.putExtra("eventname",regEvent[position]);
                    startActivity(i);

                }
            });
            AddData();
            NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);


            mAdapter.setNdefPushMessageCallback(this, this);

        }

    }
    public void AddData() {
        menu = (FloatingActionButton) findViewById(R.id.addbtn);
        menu.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(NfcActivity.this);

                        builderSingle.setTitle("Spot registration");

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NfcActivity.this, android.R.layout.simple_list_item_1);
                        arrayAdapter.add("NFC");
                        arrayAdapter.add("QR");

                        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                /*AlertDialog.Builder builderInner = new AlertDialog.Builder(NfcActivity.this);
                                builderInner.setMessage(strName);
                                builderInner.setTitle("Your Selected Item is");
                                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builderInner.show();*/
                                if(strName.equals("NFC")) {
                                    Intent i = new Intent(getApplicationContext(), spotR_NFC.class);
                                    // sending data to new activity
                                    //i.putExtra("event", info[position+5]);
                                    i.putExtra("reg", info[0]);
                                    //i.putExtra("eventname",regEvent[position]);
                                    startActivity(i);
                                }
                                else{
                                    Intent i = new Intent(getApplicationContext(), spotR_QR.class);
                                    // sending data to new activity
                                    //i.putExtra("event", info[position+5]);
                                    i.putExtra("reg", info[0]);
                                    //i.putExtra("eventname",regEvent[position]);
                                    startActivity(i);
                                }
                            }
                        });
                        builderSingle.show();
                    }
                }
        );
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
                Intent intent = new Intent(NfcActivity.this, MainActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = info[0];
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {

    }
}

