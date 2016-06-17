package com.example.paholik.personalscheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RecommendEventsActivity extends AppCompatActivity {

    public static final String LOG_TAG = "RecommendEventsActivity";

    long userID;
    /**
     * tag list of the user
     * */
    List<Tag> tagList;
    /**
     * tag's IDs of the user
     * */
    List<String> userTagIDList;
    ListView listViewRecommendEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_events);

        // TODO initialize listViewRecommendEvents

        // get current user ID
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            userID = extras.getLong(General.ARG_USER_ID);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        tagList = new ArrayList<>();
        userTagIDList = new ArrayList<>();
        List<UserTag> userTagList = UserTag.find(UserTag.class, "user_ID = ?", Long.toString(userID));
        StringBuilder stringBuilder = new StringBuilder();

        List<ExternalEvent> externalEventList = new ArrayList<>();

        // if user has chosen at least one tag in settings
        if(!userTagList.isEmpty()) {
            // get user's tags of interest
            for (UserTag userTag : userTagList) {
                Tag tag = Tag.findById(Tag.class, userTag.tagID);

                if (tag != null) {
                    if (stringBuilder.length() != 0) {
                        stringBuilder.append(" or ");
                    }
                    stringBuilder.append("tag_ID = ?");
                    tagList.add(tag);
                    userTagIDList.add(Long.toString(tag.getId()));
                }
            }

            String query = stringBuilder.toString();
            LogUtils.d("RecommendEventsActivity", "query: " + query);
            List<EventTag> eventTagList = ExternalEvent.find(EventTag.class, query, userTagIDList.toArray(new String[userTagIDList.size()]));


            for (EventTag eventTag : eventTagList) {
                ExternalEvent externalEvent = ExternalEvent.findById(ExternalEvent.class, eventTag.eventID);

                if (externalEvent != null) {
                    externalEventList.add(externalEvent);
                }
            }
        }
        else {
            externalEventList = ExternalEvent.listAll(ExternalEvent.class);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, externalEventList);

        listViewRecommendEvents = (ListView) findViewById(R.id.listViewRecommendEvents);
        listViewRecommendEvents.setAdapter(arrayAdapter);
        listViewRecommendEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private ArrayList<ExternalEvent> list;

            public AdapterView.OnItemClickListener init(List<ExternalEvent> _list) {
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
        }.init(externalEventList));
    }
}
