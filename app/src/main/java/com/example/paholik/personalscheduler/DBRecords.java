package com.example.paholik.personalscheduler;

import java.util.ArrayList;

public class DBRecords {
    public ArrayList<Integer> ids;
    public ArrayList<String> titles;

    public DBRecords(ArrayList<Integer> _ids, ArrayList<String> _titles) {
        ids = _ids;
        titles = _titles;
    }
}
