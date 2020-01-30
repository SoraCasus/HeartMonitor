package com.cmput301A1.heartmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class EditActivity extends FragmentActivity implements IOnSetDateListener, IOnTimeSetListener {

    private DataEntry entry;
    private int dataEntryIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.entry = null;

        populateFields(getIntent().getStringExtra("SELECTED"));

        EditText textField = findViewById(R.id.date_field);
        textField.setOnClickListener((View view) -> {
            DatePickerFragment dateFrag = new DatePickerFragment(this);
            dateFrag.show(getSupportFragmentManager(), "date_picker");
        });

        textField = findViewById(R.id.time_field);
        textField.setOnClickListener((View view) -> {
            TimePickerFragment timeFrag = new TimePickerFragment(this);
            timeFrag.show(getSupportFragmentManager(), "time_picker");
        });

    }

    public void onDeletePressed(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        // Send deleted flag to MainActivity
        intent.putExtra("DELETE_FLAG", "true");
        // Send dataEntryIndex to MainActivity for deletion
        intent.putExtra("DELETE_INDEX", dataEntryIndex);
        // Switch back to MainActivity
        startActivity(intent);
    }

    public void onCancelPressed(View view) {
        // Switch back to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onSavePressed(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        updateFields();
        // Convert the edited DataEntry to JSON
        try {
            JSONObject obj = entry.toJSON();
            // Send edited JSON data to MainActivity
            intent.putExtra("EDIT_DATA", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send edit flag to MainActivity
        intent.putExtra("EDIT_FLAG", "true");
        intent.putExtra("EDIT_INDEX", dataEntryIndex);

        // Switch back to MainActivity
        startActivity(intent);
    }

    private void updateFields() {

        String date = ((EditText) findViewById(R.id.date_field)).getText().toString();
        String[] dateTokens = date.split("-");
        DateTime dt = new DateTime();
        dt.year = Integer.parseInt(dateTokens[0]);
        dt.month = Integer.parseInt(dateTokens[1]);
        dt.day = Integer.parseInt(dateTokens[2]);

        String time = ((EditText) findViewById(R.id.time_field)).getText().toString();
        dateTokens = time.split(":");
        dt.hour = Integer.parseInt(dateTokens[0]);
        dt.minute = Integer.parseInt(dateTokens[1]);

        int systolic = Integer.parseInt(((EditText) findViewById(R.id.systolic_field)).getText().toString());
        int diastolic = Integer.parseInt(((EditText) findViewById(R.id.diastolic_field)).getText().toString());
        int heartRate = Integer.parseInt(((EditText) findViewById(R.id.heartRate_field)).getText().toString());
        String comment = ((EditText) findViewById(R.id.comment_field)).getText().toString();

        entry = new DataEntry(dt, systolic, diastolic, heartRate, comment);
    }

    /**
     * Parses the JSON data passed from {@see MainActivity} and fills fields
     * with the data given
     *
     * @param jsonData - The JSON String to be parsed
     */
    private void populateFields(String jsonData) {

        try {
            JSONObject obj = new JSONObject(jsonData);

            // Pull out the date/time data from JSON string
            DateTime dateTime = new DateTime();
            dateTime.year = obj.getInt("year");
            dateTime.month = obj.getInt("month");
            dateTime.day = obj.getInt("day");
            dateTime.hour = obj.getInt("hour");
            dateTime.minute = obj.getInt("minute");

            // Pull out the heart data from JSON string
            int systolic = obj.getInt("systolic");
            int diastolic = obj.getInt("diastolic");
            int heartRate = obj.getInt("heartRate");
            String comment = obj.getString("comment");

            this.dataEntryIndex = obj.getInt("index");

            // Store the data into a Data Entry for editing
            entry = new DataEntry(dateTime, systolic, diastolic, heartRate, comment);
        } catch (JSONException e) {
            e.printStackTrace();
            // Todo(Sora): Better error handling
            return;
        }


        // Set the Date field
        DateTime dateTime = entry.getDate();
        ((EditText) findViewById(R.id.date_field))
                .setText(String.format(Locale.CANADA, "%4d-%02d-%02d", dateTime.year, dateTime.month, dateTime.day));

        // Set the Time field
        ((EditText) findViewById(R.id.time_field))
                .setText(String.format(Locale.CANADA, "%02d:%02d", dateTime.hour, dateTime.minute));

        // Set the Systolic/Diastolic fields

        ((EditText) findViewById(R.id.systolic_field))
                .setText(String.format(Locale.CANADA, "%d", entry.getSystolic()));

        ((EditText) findViewById(R.id.diastolic_field))
                .setText(String.format(Locale.CANADA, "%d", entry.getDiastolic()));

        // Set the Heart Rate field
        ((EditText) findViewById(R.id.heartRate_field))
                .setText(String.format(Locale.CANADA, "%d", entry.getHeartRate()));

        // Set the comment field
        ((EditText) findViewById(R.id.comment_field))
                .setText(entry.getComment());

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ((EditText) findViewById(R.id.time_field)).setText(String.format(Locale.CANADA,
                "%02d:%02d", hourOfDay, minute));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        ((EditText) findViewById(R.id.date_field)).setText(String.format(Locale.CANADA,
                "%04d-%02d-%02d", year, (month + 1), day));
    }
}















