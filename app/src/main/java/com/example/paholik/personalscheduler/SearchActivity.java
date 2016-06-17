package com.example.paholik.personalscheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public static final String LOG_TAG = "SearchActivity";

    long userID;
    EditText editTextSearch;
    ListView listViewSearchedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // get current user ID
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            userID = extras.getLong(General.ARG_USER_ID);
        }

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
    }

    public void search(View view) {
        String query = editTextSearch.getText().toString();

        List<ExternalEvent> externalEventList = Select.from(ExternalEvent.class)
                .where(Condition.prop("name").like("%"+query+"%"))
                .or(Condition.prop("description").like("%"+query+"%"))
                .or(Condition.prop("place").like("%"+query+"%"))
                .list();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, externalEventList);

        listViewSearchedEvents = (ListView) findViewById(R.id.listViewSearchedEvents);
        listViewSearchedEvents.setAdapter(arrayAdapter);
        listViewSearchedEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
