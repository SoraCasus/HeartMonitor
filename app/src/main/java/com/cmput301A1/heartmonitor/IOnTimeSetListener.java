package com.cmput301A1.heartmonitor;

import android.widget.TimePicker;

public interface IOnTimeSetListener {

    void onTimeSet(TimePicker view, int hourOfDay, int minute);

}
