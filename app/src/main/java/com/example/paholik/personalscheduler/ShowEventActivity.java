package com.example.paholik.personalscheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ShowEventActivity extends AppCompatActivity {

    public static final String LOG_TAG = "ShowEventsActivity";
    TextView textViewEventTitle;
    TextView textViewEventDate;
    TextView textViewEventDesc;
    Button buttonChangeEventState;

    long userID;
    long eventID;
    ExternalEvent externalEvent;
    List<UserEvent> userEventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        // find elements in XML layout
        textViewEventTitle = (TextView) findViewById(R.id.eventTitle);
        textViewEventDate = (TextView) findViewById(R.id.eventDate);
        textViewEventDesc = (TextView) findViewById(R.id.eventDesc);
        buttonChangeEventState = (Button) findViewById(R.id.buttonChangeEventState);

        // get current user ID and event ID
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            userID = extras.getLong(General.ARG_USER_ID);
            eventID = extras.getLong(General.ARG_EVENT_ID);
        }

        externalEvent = ExternalEvent.findById(ExternalEvent.class, eventID);
        if(externalEvent != null) {
            textViewEventTitle.setText(externalEvent.name);

            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            String formattedDateFrom = df.format(externalEvent.getDateFrom());
            String formattedDateTo = df.format(externalEvent.getDateTo());

            textViewEventDate.setText(formattedDateFrom + " - " + formattedDateTo);
            textViewEventDesc.setText(externalEvent.description);

            userEventList = UserEvent.find(UserEvent.class, "user_ID = ? and event_ID = ?", Long.toString(userID), Long.toString(eventID));
            if(!userEventList.isEmpty()) {
                buttonChangeEventState.setText("Remove from my calendar");
            }
            else {
                buttonChangeEventState.setText("Add to my calendar");
            }
        }
    }

    public void changeEventState(View view) {
        if(userEventList.isEmpty()) {
            UserEvent userEvent = new UserEvent(userID, eventID);
            userEvent.save();
            userEventList.add(userEvent);

            buttonChangeEventState.setText("Remove from my calendar");
        }
        else {
            for(UserEvent userEvent : userEventList) {
                userEvent.delete();
            }
            userEventList.removeAll(userEventList);

            buttonChangeEventState.setText("Add to my calendar");
        }
    }
}
