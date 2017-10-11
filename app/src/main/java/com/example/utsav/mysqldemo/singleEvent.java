package com.example.utsav.mysqldemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class singleEvent extends AppCompatActivity implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback{
    private TextView status,id,name;
    public String event,reg,eventname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_event);
        status = (TextView) findViewById(R.id.nfcstatus);
        id = (TextView) findViewById(R.id.eventid);
        name = (TextView) findViewById(R.id.eventTitle);


        Intent i = getIntent();
        event = i.getStringExtra("event");
        reg = i.getStringExtra("reg");
        eventname = i.getStringExtra("eventname");
        id.setText(event);
        name.setText(eventname);
        // displaying selected product name
        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            status.setText("Sorry this device does not have NFC.");
            return;
        }
        else
            status.setText("NFC available");

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = reg+event;
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
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
                Intent intent = new Intent(singleEvent.this, MainActivity.class);
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
    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
    }

    public void generate(View v){
        Toast.makeText(this,reg, Toast.LENGTH_SHORT);
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(reg, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) findViewById(R.id.image_view)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
