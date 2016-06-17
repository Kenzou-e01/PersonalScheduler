package com.example.paholik.personalscheduler;

import com.orm.SugarRecord;

public class UserTag extends SugarRecord {
    long userID;
    long tagID;

    public UserTag() {}

    public UserTag(long _userID, long _tagID) {
        userID = _userID;
        tagID = _tagID;
    }
}
