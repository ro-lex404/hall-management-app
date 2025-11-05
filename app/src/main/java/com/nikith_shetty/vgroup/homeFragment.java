package com.nikith_shetty.vgroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import java.util.List;
import adapters.RVAdapter_home;
import helper.classes.DatabaseHelper;
import helper.classes.Global;
import models.EventData;

/**
 * This fragment now displays a list of halls loaded from the local SQLite database.
 */
public class homeFragment extends Fragment {

    private View view;
    private List<EventData> hallList;
    private RecyclerView rv;
    private RVAdapter_home rvAdapterHome;
    private Context context;
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
        this.context = context;
        if (context instanceof appTitleInterface) {
            appTitleInterface = (appTitleInterface) context;
        } else {
            throw new IllegalArgumentException("Activity must implement appTitleInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Load hall data directly from the database
        loadHallsFromDatabase();
        setUpRecyclerView();

        return view;
    }

    private void loadHallsFromDatabase() {
        if (context != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            hallList = dbHelper.getAllHalls();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appTitleInterface != null) {
            appTitleInterface.onSetTitle("Available Halls");
        }
        // Refresh the list every time the screen is shown
        loadHallsFromDatabase();
        rvAdapterHome.notifyDataSetChanged();
    }

    private void setUpRecyclerView() {
        rv = view.findViewById(R.id.home_recyclerView);
        rv.setHasFixedSize(true);
        rvAdapterHome = new RVAdapter_home(hallList);
        rvAdapterHome.setListener(data -> {
            // This part still uses the old EventDetailsActivity, which is fine for now.
            // It will just display the hall details.
            Global.setRecent(data);
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                intent.putExtra("data", new Gson().toJson(data));
                getActivity().startActivity(intent);
            }
        });
        rv.setAdapter(rvAdapterHome);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    public interface homeFragmentListener {
    }
}
