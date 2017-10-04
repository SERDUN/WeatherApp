package com.example.dmitro.weatherapp.screen.shared_dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.dmitro.weatherapp.R;

/**
 * Created by dmitro on 01.10.17.
 */

public class SharedDialog extends DialogFragment {

    private SharedAction listener;


    public interface SharedAction {
        public void shared(int i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setTitle("Куда шарити (Замінити на ресурс))")
//                .setItems(R.array.social, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        listener = (SharedAction) getParentFragment();
//                        listener.shared(which);
//
//                    }
//                });

        return builder.create();
    }


}
