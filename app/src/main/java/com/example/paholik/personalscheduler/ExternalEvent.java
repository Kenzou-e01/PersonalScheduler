package com.example.paholik.personalscheduler;

import com.orm.SugarRecord;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExternalEvent extends SugarRecord {
    String name;
    String description;
    private long dateFrom;
    private long dateTo;
    String place;

    public ExternalEvent() {}

    public ExternalEvent(String _name, String _description, Date _from, Date _to, String _place) {
        name = _name;
        description = _description;
        dateFrom = _from.getTime();
        dateTo = _to.getTime();
        place = _place;
    }

    public Date getDateFrom() {
        return new Date(dateFrom);
    }

    public Date getDateTo() {
        return new Date(dateTo);
    }

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(getDateFrom());
        return formattedDate + " " + name;
    }
}
