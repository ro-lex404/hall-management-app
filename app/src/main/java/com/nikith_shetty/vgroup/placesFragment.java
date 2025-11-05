package com.nikith_shetty.vgroup;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
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
import adapters.RVAdapter_halls;
import helper.classes.Global;
import helper.classes.HTTPhelper;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class placesFragment extends Fragment {

    private View view;
    private ProgressDialog progressDialog;
    private JSONArray jsonArray;
    private RecyclerView rv;
    private RVAdapter_halls rvAdapter_halls;
    private Context context;
    private appTitleInterface appTitleInterface;

    public placesFragment() {
        // Required empty public constructor
    }

    public static placesFragment newInstance() {
        return new placesFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof appTitleInterface) {
            appTitleInterface = (appTitleInterface) context;
        } else {
            throw new IllegalArgumentException("Activity must implement appTitleInterface");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places, container, false);

        if (context != null) {
            progressDialog = ProgressDialog.show(context, "", "Loading...");
        }

        new Thread(() -> {
            try {
                ResponseBody body = HTTPhelper.get(Global.GET_PLACES_DATA).body();
                jsonArray = convertFromInputStreamToJsonArray(body);
                if (context != null) {
                    Intent intent = new Intent(Global.ACTION_DATA_RECEIVED);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appTitleInterface != null) {
            appTitleInterface.onSetTitle("Halls");
        }
        if (context != null) {
            IntentFilter eventDataReceived = new IntentFilter(Global.ACTION_DATA_RECEIVED);
            LocalBroadcastManager.getInstance(context).registerReceiver(onEventDataReceivedPlaces, eventDataReceived);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (context != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(onEventDataReceivedPlaces);
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public JSONArray convertFromInputStreamToJsonArray(ResponseBody responseBody) {
        if (responseBody == null) return null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseBody.byteStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return new JSONArray(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private final BroadcastReceiver onEventDataReceivedPlaces = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (isAdded() && view != null) {
                view.invalidate();
                setUpView();
            }
        }
    };

    private void setUpView() {
        rv = view.findViewById(R.id.recyclerView_places);
        rv.setHasFixedSize(true);
        rvAdapter_halls = new RVAdapter_halls(jsonArray);
        rvAdapter_halls.setListener(this::makeTransactionToEventsFragment);
        rv.setAdapter(rvAdapter_halls);
        rv.setLayoutManager(new LinearLayoutManager(context));
    }

    private void makeTransactionToEventsFragment(String data) {
        if (getActivity() != null) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_area, eventFragment.newInstance(eventFragment.PLACES_FILTER, data));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public interface placesFragmentListener {
    }
}
