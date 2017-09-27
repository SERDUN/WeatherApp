package com.example.dmitro.weatherapp.screen.failure;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.utils.callback.Action0;

/**
 * Created by dmitro on 27.09.17.
 */

public class FailureDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private Action0 callback;

    public void setCallback(Action0 callback) {
        this.callback = callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.yes, this)
                .setMessage(R.string.message_text);
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case Dialog.BUTTON_POSITIVE:
                callback.call();
                break;


        }
    }
}
