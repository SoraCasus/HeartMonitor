package com.cmput301A1.heartmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements IAddEditDialogListener {

    private static final String SAVE_FILE_NAME = "heart_monitor.json";

    private List<DataEntry> dataEntries;
    private DataEntryAdapter dataEntryAdapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateListView();

        // Retrieve data from intent
        String editFlag = getIntent().getStringExtra("EDIT_FLAG");
        String deleteFlag = getIntent().getStringExtra("DELETE_FLAG");
        if (editFlag != null) {
            String jsonData = getIntent().getStringExtra("EDIT_DATA");
            int editedIndex = getIntent().getIntExtra("EDIT_INDEX", -1);
            loadEditedData(jsonData, editedIndex);
        } else if (deleteFlag != null) {
            int indexToDelete = getIntent().getIntExtra("DELETE_INDEX", -1);

        }

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            // Create the Intent, send data to the new Activity
            Intent intent = new Intent(this, EditActivity.class);
            DataEntry entry = dataEntries.get(position);
            JSONObject obj = null;
            try {
                obj = entry.toJSON();
                obj.put("year", entry.getDate().year);
                obj.put("month", entry.getDate().month);
                obj.put("day", entry.getDate().day);
                obj.put("hour", entry.getDate().hour);
                obj.put("minute", entry.getDate().minute);
                obj.put("systolic", entry.getSystolic());
                obj.put("diastolic", entry.getDiastolic());
                obj.put("heartRate", entry.getHeartRate());
                obj.put("comment", entry.getComment());
                obj.put("index", position);

                // TODO(Sora): Store all entries to JSON
                String serializedData = serializeData();

                if (serializedData != null)
                    outputToFile(serializedData);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            assert obj != null;

            Log.d("HEART", obj.toString());
            intent.putExtra("SELECTED", obj.toString());
            startActivity(intent);
        });
    }

    private void loadEditedData(String data, int index) {
        try {
            DataEntry entry = new DataEntry(new JSONObject(data));
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Opens a FileOutputStream and stores the list of values to the file
     *
     * @param toOutput - The serialized data to be saved
     */
    private void outputToFile(String toOutput) {
        if (toOutput != null) {
            try (FileOutputStream fos = getApplicationContext().openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE)) {
                fos.write(toOutput.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String serializeData() throws JSONException {
        if (dataEntries.size() == 0) return null;
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        for (int i = 0; i < dataEntries.size(); i++) {
            DataEntry d = dataEntries.get(i);
            JSONObject o = new JSONObject();
            o.put("year", d.getDate().year);
            o.put("month", d.getDate().month);
            o.put("day", d.getDate().day);
            o.put("hour", d.getDate().hour);
            o.put("minute", d.getDate().minute);
            o.put("systolic", d.getSystolic());
            o.put("diastolic", d.getDiastolic());
            o.put("heartRate", d.getHeartRate());
            o.put("comment", d.getComment());
            arr.put(i, o);
        }
        obj.put("entries", arr);

        return obj.toString();
    }

    private void populateListView() {
        dataEntries = new ArrayList<>();

        try {
            FileInputStream fis = getApplicationContext().openFileInput(SAVE_FILE_NAME);
            InputStreamReader isReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(isReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            }

            JSONObject saveData = new JSONObject(sb.toString());
            JSONArray array = saveData.getJSONArray("entries");
            for (int i = 0; i < array.length(); i++) {
                JSONObject entry = array.getJSONObject(i);
                DateTime dt = new DateTime();
                dt.year = entry.getInt("year");
                dt.month = entry.getInt("month");
                dt.day = entry.getInt("day");
                dt.hour = entry.getInt("hour");
                dt.minute = entry.getInt("minute");

                int systolic = entry.getInt("systolic");
                int diastolic = entry.getInt("diastolic");
                int heartRate = entry.getInt("heartRate");
                String comment = entry.getString("comment");
                DataEntry dataEntry = new DataEntry(dt, systolic, diastolic, heartRate, comment);
                dataEntries.add(dataEntry);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        listView = findViewById(R.id.list_view);
        dataEntryAdapter = new DataEntryAdapter(this, dataEntries);
        listView.setAdapter(dataEntryAdapter);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
