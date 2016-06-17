package com.example.paholik.personalscheduler;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Tag extends SugarRecord {
    @Unique
    String name;

    public Tag() {}

    public Tag(String _name) {
        name = _name;
    }
}
