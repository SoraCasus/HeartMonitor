package com.cmput301A1.heartmonitor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditDialogFragment extends DialogFragment {

    private IAddEditDialogListener listener;
    private DatePicker picker;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.listener = (IAddEditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement" +
                    "IAddEditDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(dialogView);

        final EditText dateInput = dialogView.findViewById(R.id.edit_date);
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
            dateInput.setText(sdf.format(calendar.getTime()));
        };

        dateInput.setOnClickListener((View view) -> {
            new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        builder.setCancelable(false)
                .setPositiveButton("Save", (DialogInterface dialog, int id) -> {
                    listener.onDialogPositiveClick(this);
                })
                .setNegativeButton("Cancel", (DialogInterface dialog, int id) -> {
                    listener.onDialogNegativeClick(this);
                    dialog.cancel();
                });

        return builder.create();
    }

}
