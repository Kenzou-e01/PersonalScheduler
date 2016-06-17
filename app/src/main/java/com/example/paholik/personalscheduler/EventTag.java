package com.example.paholik.personalscheduler;

import com.orm.SugarRecord;

public class EventTag extends SugarRecord {
    long eventID;
    long tagID;

    public EventTag() {}

    public EventTag(long _eventID, long _tagID) {
        eventID = _eventID;
        tagID = _tagID;
    }
}
