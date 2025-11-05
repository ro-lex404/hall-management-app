package com.hallbooking.app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import adapters.RVAdapter_departments;
import helper.classes.Global;
import helper.classes.HTTPhelper;
import okhttp3.ResponseBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class collegesFragment extends Fragment {

    View view;
    ProgressDialog progressDialog;
    JSONArray jsonArray;
    RecyclerView rv;
    RVAdapter_departments rvAdapter_departments;
    LinearLayoutManager layout;
    Context context;
    appTitleInterface appTitleInterface;

    public collegesFragment() {
        // Required empty public constructor
    }

    public static collegesFragment newInstance(){
        return new collegesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        final Activity activity = getActivity();
        if (activity instanceof appTitleInterface) {
            appTitleInterface = (appTitleInterface) activity;
        } else {
            throw new IllegalArgumentException("Activity must implement appTitleInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_colleges, container, false);

        /* Fetch college list from server
         *
         */
        progressDialog = ProgressDialog.show(context, "", "Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    jsonArray = convertFromInputStreamToJsonobject(HTTPhelper.get(Global.GET_COLLEGES_DATA).body());
                    Intent intent = new Intent(Global.ACTION_DATA_RECEIVED);
                    if(context != null) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        appTitleInterface.onSetTitle("Departments");
        IntentFilter eventDataReceived = new IntentFilter(Global.ACTION_DATA_RECEIVED);
        LocalBroadcastManager.getInstance(context).registerReceiver(onEventDataReceivedPlaces, eventDataReceived);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(onEventDataReceivedPlaces);
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public JSONArray convertFromInputStreamToJsonobject(ResponseBody responseBody){
        if (responseBody == null) return null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseBody.byteStream()));
        JSONArray jsonArray = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            jsonArray = new JSONArray(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private BroadcastReceiver onEventDataReceivedPlaces = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(isAdded()) {
                view.invalidate();
                setUpView();
            }
        }
    };

    private void setUpView() {
        rv = (RecyclerView) view.findViewById(R.id.recyclerView_colleges);
        rv.setHasFixedSize(true);
        rvAdapter_departments = new RVAdapter_departments(jsonArray);
        rvAdapter_departments.setListener(new RVAdapter_departments.Listener() {
            @Override
            public void onClick(String data) {
                makeTransactionToEventsFragment(data);
            }
        });
        rv.setAdapter(rvAdapter_departments);
        layout = new LinearLayoutManager(context);
        rv.setLayoutManager(layout);
    }

    private void makeTransactionToEventsFragment(String data) {
        if(getActivity() != null) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_area, eventFragment.newInstance(eventFragment.COLLEGES_FILTER, data));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public interface collegesFragmentListener{
    }

}
