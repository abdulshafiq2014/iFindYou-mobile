package com.example.hp.myapplication;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by chongcher on 13/11/2016.
 */

public class AlertDialogFragment extends DialogFragment {

    public static AlertDialogFragment newInstance() {
        return new AlertDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_view_volunteer, container, false);
        View tv = v.findViewById(R.id.content_id);
        ((TextView)tv).setText("Found a beacon!");
        return v;
    }

}
