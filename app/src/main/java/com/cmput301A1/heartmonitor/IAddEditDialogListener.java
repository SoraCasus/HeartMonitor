package com.cmput301A1.heartmonitor;

import androidx.fragment.app.DialogFragment;

public interface IAddEditDialogListener {

    void onDialogPositiveClick(DialogFragment dialog);

    void onDialogNegativeClick(DialogFragment dialog);
}
