package com.hallbooking.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This fragment now displays a simple welcome message.
 */
public class homeFragment extends Fragment {

    private appTitleInterface appTitleInterface;

    public homeFragment() {
        // Required empty public constructor
    }

    public static homeFragment newInstance() {
        return new homeFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof appTitleInterface) {
            appTitleInterface = (appTitleInterface) context;
        } else {
            throw new IllegalArgumentException("Activity must implement appTitleInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the new welcome layout for this fragment
        return inflater.inflate(R.layout.fragment_home_new, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appTitleInterface != null) {
            appTitleInterface.onSetTitle("Home");
        }
    }
}
