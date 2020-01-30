package com.cmput301A1.heartmonitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<DataEntry> dataEntries;
    private DataEntryAdapter dataEntryAdapter;
    private ListView listView;

    private SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);


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
            if (indexToDelete > -1) {
                dataEntries.remove(indexToDelete);
                dataEntryAdapter.notifyDataSetChanged();
                try {
                    String serializedData = serializeData();
                    sharedPrefs.edit().putString("save", serializedData).apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            // Create the Intent, send data to the new Activity
            Intent intent = new Intent(this, EditActivity.class);
            DataEntry entry = dataEntries.get(position);
            JSONObject obj = null;
            try {
                obj = entry.toJSON();
                obj.put("index", position);

                // TODO(Sora): Store all entries to JSON
                String serializedData = serializeData();

                if (serializedData != null)
                    sharedPrefs.edit().putString("save", serializedData).apply();

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
            Log.d("HEART", "Adding " + data);
            if (dataEntries.size() > index)
                dataEntries.remove(index);
            DataEntry entry = new DataEntry(new JSONObject(data));
            dataEntries.add(index, entry);

            String serializedData = serializeData();
            if (serializedData != null)
                sharedPrefs.edit().putString("save", serializedData).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String serializeData() throws JSONException {
        if (dataEntries.size() == 0) return null;
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        for (int i = 0; i < dataEntries.size(); i++) {
            DataEntry d = dataEntries.get(i);
            JSONObject o = d.toJSON();
            arr.put(i, o);
        }
        obj.put("entries", arr);

        return obj.toString();
    }

    private void populateListView() {
        dataEntries = new ArrayList<>();

        try {
            String saveInfo = sharedPrefs.getString("save", null);
            if (saveInfo == null)
                throw new JSONException("No save file exists");

            JSONObject obj = new JSONObject(saveInfo);
            JSONArray arr = obj.getJSONArray("entries");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                DataEntry d = new DataEntry(o);

                dataEntries.add(d);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView = findViewById(R.id.list_view);
        dataEntryAdapter = new DataEntryAdapter(this, dataEntries);
        listView.setAdapter(dataEntryAdapter);
    }

    public void onAddPressed(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        Calendar cal = Calendar.getInstance();
        DateTime dt = new DateTime();
        dt.year = cal.get(Calendar.YEAR);
        dt.month = cal.get(Calendar.MONTH) + 1;
        dt.day = cal.get(Calendar.DAY_OF_MONTH);
        dt.hour = cal.get(Calendar.HOUR_OF_DAY);
        dt.minute = cal.get(Calendar.MINUTE);

        DataEntry de = new DataEntry(dt, 0, 0, 0, "");
        try {
            JSONObject obj = de.toJSON();
            obj.put("index", dataEntries.size());
            intent.putExtra("SELECTED", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(intent);
    }
}
