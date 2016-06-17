package com.example.paholik.personalscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyEventsActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MyEventsActivity";
    private ListView listView;
    private long userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LogUtils.d(LOG_TAG, "got here");

        // get current user ID
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            userID = extras.getLong(General.ARG_USER_ID);
            LogUtils.d(LOG_TAG, "- userID: " + userID);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<UserEvent> userEventsList = UserEvent.find(UserEvent.class, "user_ID = ?", Long.toString(userID));
        ArrayList<ExternalEvent> list = new ArrayList<>();

        for(UserEvent userEvent : userEventsList) {
            LogUtils.d(LOG_TAG, "- userEventID: " + userEvent.eventID);
            ExternalEvent externalEvent = ExternalEvent.findById(ExternalEvent.class, userEvent.eventID);
            if(externalEvent != null) {
                list.add(externalEvent);
            }
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private ArrayList<ExternalEvent> list;

            public AdapterView.OnItemClickListener init(ArrayList<ExternalEvent> _list) {
                list = new ArrayList<>(_list);
                return this;
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExternalEvent selectedEvent = list.get(position);

                LogUtils.d(LOG_TAG, "- selected position: " + position);
                LogUtils.d(LOG_TAG, "- selected : " + selectedEvent.getId());

                Intent intent = new Intent(getApplicationContext(), ShowEventActivity.class);
                intent.putExtra(General.ARG_USER_ID, userID);
                intent.putExtra(General.ARG_EVENT_ID, selectedEvent.getId());
                startActivity(intent);
            }
        }.init(list));
    }
}
