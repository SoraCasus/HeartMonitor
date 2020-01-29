package com.cmput301A1.heartmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    private DataEntry entry;
    private int dataEntryIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.entry = null;

        populateFields(getIntent().getStringExtra("SELECTED"));

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
        // Convert the edited DataEntry to JSON

        // Send edit flag to MainActivity
        // Send edited JSON data to MainActivity
        // Switch back to MainActivity
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
                .setText(String.format(Locale.CANADA, "%d-%d-%d", dateTime.year, dateTime.month, dateTime.day));

        // Set the Time field
        ((EditText) findViewById(R.id.time_field))
                .setText(String.format(Locale.CANADA, "%2d:%2d", dateTime.hour, dateTime.minute));

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
}















