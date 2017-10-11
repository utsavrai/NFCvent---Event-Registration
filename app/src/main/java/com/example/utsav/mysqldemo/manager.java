package com.example.utsav.mysqldemo;
import static com.example.utsav.mysqldemo.Constants.FIRST_COLUMN;
import static com.example.utsav.mysqldemo.Constants.FOURTH_COLUMN;
import static com.example.utsav.mysqldemo.Constants.SECOND_COLUMN;
import static com.example.utsav.mysqldemo.Constants.THIRD_COLUMN;
import static com.example.utsav.mysqldemo.Constants.FIFTH_COLUMN;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Utsav on 21-Apr-17.
 */

public class manager extends AppCompatActivity {
    String data;
    private ArrayList<HashMap<String, String>> list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_manager);
        Intent i = getIntent();
        data = i.getStringExtra("data");
        String[] eve = data.split(" ");
        ListView listView=(ListView)findViewById(R.id.listView1);

        list=new ArrayList<HashMap<String,String>>();
        for(int x =0;x<eve.length/5;x++){
            int k = 5*x;
            HashMap<String,String> temp=new HashMap<String, String>();
            temp.put(FIRST_COLUMN, eve[0+k]);
            temp.put(SECOND_COLUMN, eve[1+k]);
            temp.put(THIRD_COLUMN, eve[2+k]);
            temp.put(FOURTH_COLUMN,eve[3+k]);
            temp.put(FIFTH_COLUMN, eve[4+k]);
            list.add(temp);
        }

        ListViewAdapter adapter=new ListViewAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(manager.this, Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
