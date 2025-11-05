package com.hallbooking.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class accountsFragment extends Fragment {

    private appTitleInterface appTitleInterface;

    public accountsFragment() {
        // Required empty public constructor
    }

    public static accountsFragment newInstance() {
        return new accountsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof appTitleInterface) {
            appTitleInterface = (appTitleInterface) context;
        } else {
            throw new IllegalArgumentException("Activity must implement appTitleInterface");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appTitleInterface != null) {
            appTitleInterface.onSetTitle("My Bookings");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        appTitleInterface = null;
    }

    public interface accountFragmentListener {
    }
}
