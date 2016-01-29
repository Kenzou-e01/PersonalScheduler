package com.example.paholik.personalscheduler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DisplayEventActivity extends AppCompatActivity {

    int fromWhereIAmComing = 0;
    private DBHelper db;

    private TextView title;
    private TextView desc;
    private TextView date;
    private Button setDateButton;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    int idToUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.editTextTitle);
        desc = (TextView) findViewById(R.id.editTextDesc);
        date = (TextView) findViewById(R.id.showDate);
        setDateButton = (Button) findViewById(R.id.setDate);

        db = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int value = extras.getInt("id");

            if(value > 0) { // the view part, not the add event part
                LogUtils.d("DisplayEventActivity", "value = " + value);
                Cursor rs = db.getData(value);
                LogUtils.d("DisplayEventActivity", "got some values");
                idToUpdate = value;
                rs.moveToFirst();
                LogUtils.d("DisplayEventActivity", "moved to first");

                String tit = rs.getString(rs.getColumnIndex(DBHelper.EventTable.EVENTS_COLUMN_TITLE));
                String des = rs.getString(rs.getColumnIndex(DBHelper.EventTable.EVENTS_COLUMN_DESC));
                String dat = rs.getString(rs.getColumnIndex(DBHelper.EventTable.EVENTS_COLUMN_DATE));

                LogUtils.d("DisplayEventActivity", "set strings");

                if(!rs.isClosed()) {
                    rs.close();
                }

                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                title.setText((CharSequence) tit);
                title.setFocusable(false);
                title.setClickable(false);

                desc.setText((CharSequence) des);
                desc.setFocusable(false);
                desc.setClickable(false);

                date.setText((CharSequence) dat);
                setDateButton.setVisibility(View.INVISIBLE);

            }
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker dp, int y, int m, int d) {
            showDate(y, m+1, d);
        }
    };

    private void showDate(int y, int m, int d) {
        date.setText(new StringBuilder().append(d).append(".").append(m).append(".").append(y));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            int value = extras.getInt("id");
            if(value > 0) {
                getMenuInflater().inflate(R.menu.menu_display_event, menu);
            }
            else {
                getMenuInflater().inflate(R.menu.menu_my, menu);
            }
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Event:
                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                title.setEnabled(true);
                title.setFocusableInTouchMode(true);
                title.setClickable(true);

                desc.setEnabled(true);
                desc.setFocusableInTouchMode(true);
                desc.setClickable(true);

                setDateButton.setVisibility(View.VISIBLE);

                return true;
            case R.id.Delete_Event:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteEvent);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.deleteEvent(idToUpdate);
                        Toast.makeText(getApplicationContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MyActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user cacelled dialog
                    }
                });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure?");
                d.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            int value = extras.getInt("id");

            if(value > 0) {
                LogUtils.d("------- run", "value > 0");
                if(db.updateEvent(idToUpdate, title.getText().toString(), desc.getText().toString(), date.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MyActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not updated", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                LogUtils.d("------- run", "value = 0");
                String t = title.getText().toString();
                String d = desc.getText().toString();
                String _date = date.getText().toString();
                //String t = "tit";
                //String d = "des";
                LogUtils.d("------- run", "got title and desc values");
                if(db.insertEvent(t, d, _date)) {
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not done", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext(), MyActivity.class);
                startActivity(intent);
            }
        }
    }
}
