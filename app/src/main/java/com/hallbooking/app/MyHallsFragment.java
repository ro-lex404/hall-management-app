package com.hallbooking.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import adapters.RVAdapter_home;
import helper.classes.DatabaseHelper;
import models.EventData;

public class MyHallsFragment extends Fragment {

    private RecyclerView rv;
    private RVAdapter_home rvAdapterHome;
    private List<EventData> hallList = new ArrayList<>();
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        rv = view.findViewById(R.id.recyclerView_places);
        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOwnedHalls();
        rvAdapterHome.updateData(hallList);
    }

    private void loadOwnedHalls() {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        long ownerId = prefs.getLong("user_id", -1);

        if (ownerId != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            hallList = dbHelper.getHallsByOwner(ownerId);
        }
    }

    private void setupRecyclerView() {
        rvAdapterHome = new RVAdapter_home(hallList, context);
        rvAdapterHome.setListener(data -> {
            Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
            intent.putExtra("data", new Gson().toJson(data));
            startActivity(intent);
        });
        rv.setAdapter(rvAdapterHome);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }
}
