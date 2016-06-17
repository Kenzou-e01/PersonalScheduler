package com.example.paholik.personalscheduler;


import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;

    private Bundle savedInstanceState;

    private User user;

    void sugarDBInit() {
        // make app create all used DB tables
        User.findById(User.class, (long) 1);
        Tag.findById(Tag.class, (long) 1);
        UserTag.findById(UserTag.class, (long) 1);
        UserEvent.findById(UserEvent.class, (long) 1);
        ExternalEvent.findById(ExternalEvent.class, (long) 1);
        EventTag.findById(EventTag.class, (long) 1);
    }

    void importSampleData() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(cal.getTime());
        LogUtils.d("importSampleData", "- date: " + formattedDate);

        EventTag.deleteAll(EventTag.class);
        ExternalEvent.deleteAll(ExternalEvent.class);
        Tag.deleteAll(Tag.class);
        UserEvent.deleteAll(UserEvent.class);
        UserTag.deleteAll(UserTag.class);

        Tag t1 = new Tag("spolocenstvo");
        t1.save();
        Tag t2 = new Tag("efata");
        t2.save();
        Tag t3 = new Tag("espe");
        t3.save();
        Tag t4 = new Tag("sport");
        t4.save();
        Tag t5 = new Tag("rast");
        t5.save();

        EventTag et1;
        EventTag et2;
        EventTag et3;

        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        ExternalEvent ee1 = new ExternalEvent("Efata Chvaly PB", "Chvaly v PB", cal.getTime(), cal.getTime(), "Povazska Bystrica");
        ee1.save();
        et1 = new EventTag(ee1.getId(), t1.getId());
        et2 = new EventTag(ee1.getId(), t2.getId());
        et3 = new EventTag(ee1.getId(), t5.getId());
        et1.save();
        et2.save();
        et3.save();

        cal.set(Calendar.MONTH, 5);
        cal.set(Calendar.DAY_OF_MONTH, 2);
        ExternalEvent ee2 = new ExternalEvent("Efata Den spolocenstva PB", "Den spolocenstva Efata", cal.getTime(), cal.getTime(), "Povazska Bystrica");
        ee2.save();
        et1 = new EventTag(ee2.getId(), t1.getId());
        et2 = new EventTag(ee2.getId(), t2.getId());
        et3 = new EventTag(ee2.getId(), t5.getId());
        et1.save();
        et2.save();
        et3.save();

        cal.set(Calendar.MONTH, 4);
        cal.set(Calendar.DAY_OF_MONTH, 8);
        ExternalEvent ee3 = new ExternalEvent("Prednaska Milovat a ctit", "Richard Vasecka na temu Financie v rodine", cal.getTime(), cal.getTime(), "Zilina");
        ee3.save();
        et1 = new EventTag(ee3.getId(), t5.getId());
        et1.save();

        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DAY_OF_MONTH, 15);
        ExternalEvent ee4 = new ExternalEvent("Zlomeni", "Otvorene stretnutie spolocenstva SP", cal.getTime(), cal.getTime(), "Sliac");
        ee4.save();
        et1 = new EventTag(ee4.getId(), t1.getId());
        et2 = new EventTag(ee4.getId(), t3.getId());
        et3 = new EventTag(ee4.getId(), t5.getId());
        et1.save();
        et2.save();
        et3.save();

        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 23);
        ExternalEvent ee5 = new ExternalEvent("Tour de Efata", "Tradicna cyklotura po okoli Povazskej Bystrice", cal.getTime(), cal.getTime(), "Povazska Bystrica a okolie");
        ee5.save();
        et1 = new EventTag(ee5.getId(), t1.getId());
        et2 = new EventTag(ee5.getId(), t2.getId());
        et3 = new EventTag(ee5.getId(), t4.getId());
        et1.save();
        et2.save();
        et3.save();
        UserEvent ue = new UserEvent(user.getId(), ee5.getId());
        ue.save();
    }

    User getUser() {
        // currently always returns the same one user (with ID = 1)
        int userID = 1;
        User u = User.findById(User.class, userID);

        // if no user with id 'userID' was found
        if(u == null) {
            // create new user
            u = new User("user", "pass", new ArrayList<Tag>());
            u.save();
        }

        return u;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init DB
        sugarDBInit();

        user = getUser();
        importSampleData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        caldroidInit(savedInstanceState);
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState, "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    public void showEvents(View view) {
        // prepare information for MyEventsActivity
        Intent intent = new Intent(this, MyEventsActivity.class);
        intent.putExtra(General.ARG_USER_ID, user.getId());
        startActivity(intent);
    }

    public void recommendEvents(View view) {
        // prepare information for MyEventsActivity
        Intent intent = new Intent(this, RecommendEventsActivity.class);
        intent.putExtra(General.ARG_USER_ID, user.getId());
        startActivity(intent);
    }

    public void caldroidInit(Bundle savedInstanceState) {
        caldroidFragment = new CaldroidFragment();

        // If activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();

            // set calendar preferences
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY); // Tuesday
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            caldroidFragment.setArguments(args);
        }

        // attach calendar to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
        final CaldroidListener listener = new CaldroidListener() {
            long userID;

            public CaldroidListener init(long _userID) {
                userID = _userID;
                return this;
            }

            @Override
            public void onSelectDate(Date date, View view) {
                LogUtils.d("onSelectDate", formatter.format(date));
                // TODO when date is selected, show events scheduled to this date
            }

            @Override
            public void onChangeMonth(int month, int year) {
                // get user's events in selected month
                List<UserEvent> userEventsList = UserEvent.find(UserEvent.class, "user_ID = ?", Long.toString(userID));

                List<ExternalEvent> externalEventList = ExternalEvent.listAll(ExternalEvent.class);

                // for each of users events check if they are scheduled for current month
                for(UserEvent userEvent : userEventsList) {
                    // find event details by ID
                    LogUtils.d("onChangeMonth", "- userEvent.eventID: " + userEvent.eventID);
                    ExternalEvent externalEvent = ExternalEvent.findById(ExternalEvent.class, userEvent.eventID);

                    if(externalEvent != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(externalEvent.getDateFrom());
                        int eventMonth = cal.get(Calendar.MONTH) + 1;
                        int eventDay = cal.get(Calendar.DAY_OF_MONTH);

                        LogUtils.d("onChangeMonth", "- currentMonth: " + month);
                        LogUtils.d("onChangeMonth", "- eventMonth: " + eventMonth);

                        // if 'externalEvent' is in selected month
                        if (month == eventMonth) {
                            // color field in calendar
                            cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, year);
                            cal.set(Calendar.MONTH, month - 1);
                            cal.set(Calendar.DAY_OF_MONTH, eventDay);
                            Date dateToBeColoured = cal.getTime();
                            caldroidFragment.setBackgroundResourceForDate(R.color.blue, dateToBeColoured);
                            caldroidFragment.setTextColorForDate(R.color.white, dateToBeColoured);
                        }
                    }
                }
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                LogUtils.d("onLongClickDate", formatter.format(date));
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    LogUtils.d("onCaldroidViewCreated", "Caldroid view created");
                }
            }

        }.init(user.getId());
        // setup caldroid listener
        caldroidFragment.setCaldroidListener(listener);

        // Customize the calendar
        final TextView textView = (TextView) findViewById(R.id.textView);
        final Button customizeButton = (Button) findViewById(R.id.customize_button);

        customizeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (undo) {
                    customizeButton.setText(getString(R.string.customize));
                    textView.setText("");

                    // Reset calendar
                    caldroidFragment.clearDisableDates();
                    caldroidFragment.clearSelectedDates();
                    caldroidFragment.setMinDate(null);
                    caldroidFragment.setMaxDate(null);
                    caldroidFragment.setShowNavigationArrows(true);
                    caldroidFragment.setEnableSwipe(true);
                    caldroidFragment.refreshView();
                    undo = false;
                    return;
                }

                // Else
                undo = true;
                customizeButton.setText(getString(R.string.undo));
                Calendar cal = Calendar.getInstance();

                // Min date is last 7 days
                cal.add(Calendar.DATE, -7);
                Date minDate = cal.getTime();

                // Max date is next 7 days
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 14);
                Date maxDate = cal.getTime();

                // Set selected dates
                // From Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 2);
                Date fromDate = cal.getTime();

                // To Date
                cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 3);
                Date toDate = cal.getTime();

                // Set disabled dates
                ArrayList<Date> disabledDates = new ArrayList<Date>();
                for (int i = 5; i < 8; i++) {
                    cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, i);
                    disabledDates.add(cal.getTime());
                }

                // Customize
                caldroidFragment.setMinDate(minDate);
                caldroidFragment.setMaxDate(maxDate);
                caldroidFragment.setDisableDates(disabledDates);
                caldroidFragment.setSelectedDates(fromDate, toDate);
                caldroidFragment.setShowNavigationArrows(false);
                caldroidFragment.setEnableSwipe(false);

                caldroidFragment.refreshView();

                // Move to date
                // cal = Calendar.getInstance();
                // cal.add(Calendar.MONTH, 12);
                // caldroidFragment.moveToDate(cal.getTime());

                String text = "Today: " + formatter.format(new Date()) + "\n";
                text += "Min Date: " + formatter.format(minDate) + "\n";
                text += "Max Date: " + formatter.format(maxDate) + "\n";
                text += "Select From Date: " + formatter.format(fromDate)
                        + "\n";
                text += "Select To Date: " + formatter.format(toDate) + "\n";
                for (Date date : disabledDates) {
                    text += "Disabled Date: " + formatter.format(date) + "\n";
                }

                textView.setText(text);
            }
        });

        final Bundle state = savedInstanceState;
        Button showDialogButton = (Button) findViewById(R.id.show_dialog_button);
        showDialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Setup caldroid to use as dialog
                dialogCaldroidFragment = new CaldroidFragment();
                dialogCaldroidFragment.setCaldroidListener(listener);

                // If activity is recovered from rotation
                final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
                if (state != null) {
                    dialogCaldroidFragment.restoreDialogStatesFromKey(
                            getSupportFragmentManager(), state,
                            "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                    Bundle args = dialogCaldroidFragment.getArguments();
                    if (args == null) {
                        args = new Bundle();
                        dialogCaldroidFragment.setArguments(args);
                    }
                } else {
                    // Setup arguments
                    Bundle bundle = new Bundle();
                    // Setup dialogTitle
                    dialogCaldroidFragment.setArguments(bundle);
                }

                dialogCaldroidFragment.show(getSupportFragmentManager(), dialogTag);
            }
        });
    }

    /**
     * Create menu with items - Settings, Search
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    /**
     * Handle when menu item is clicked
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(General.ARG_USER_ID, user.getId());
                startActivity(intent);
                return true;
            case R.id.action_search:
                // TODO redirect to Search activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
