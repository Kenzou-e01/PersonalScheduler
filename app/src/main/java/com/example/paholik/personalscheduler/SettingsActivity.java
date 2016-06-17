package com.example.paholik.personalscheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    public static final String LOG_TAG = "SettingsActivity";
    EditText editTextTags;
    Button buttonSettingsSave;
    long userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // find elements in XML layout
        editTextTags = (EditText) findViewById(R.id.editTextTags);
        buttonSettingsSave = (Button) findViewById(R.id.buttonSettingsSave);

        // get current user ID
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            userID = extras.getLong(General.ARG_USER_ID);
        }

        List<UserTag> userTagList = UserTag.find(UserTag.class, "user_ID = ?", Long.toString(userID));
        StringBuilder stringBuilder = new StringBuilder();

        for(UserTag userTag : userTagList) {
            Tag tag = Tag.findById(Tag.class, userTag.tagID);

            if(tag != null) {
                if(stringBuilder.length() != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(tag.name);
            }
        }

        editTextTags.setText(stringBuilder.toString());
    }

    public void saveSettings(View view) {
        String tags = editTextTags.getText().toString();
        String[] tagArray = tags.split(",");

        // save every tag from user input
        for(String tagString : tagArray) {

            // do not insert empty strings into DB
            if(!tagString.isEmpty()) {

                // if there is already tag with the same name in the DB
                List<Tag> tagList = Tag.find(Tag.class, "name = ?", tagString);
                Tag tag;

                if(tagList.isEmpty()) {
                    tag = new Tag(tagString);
                    tag.save();
                }
                else {
                    tag = tagList.get(0);
                }

                // save this tag for current user
                List<UserTag> userTagList = UserTag.find(UserTag.class, "user_ID = ? and tag_ID = ?", Long.toString(userID), Long.toString(tag.getId()));
                if(userTagList.isEmpty()) {
                    UserTag userTag = new UserTag(userID, tag.getId());
                    userTag.save();
                }
            }
        }
    }
}
