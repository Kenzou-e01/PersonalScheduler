package com.example.paholik.personalscheduler;

import com.orm.SugarRecord;

public class UserEvent extends SugarRecord {

    long userID;
    long eventID;

    public UserEvent() {}

    public UserEvent(long _userID, long _eventID) {
        userID = _userID;
        eventID = _eventID;
    }

}
