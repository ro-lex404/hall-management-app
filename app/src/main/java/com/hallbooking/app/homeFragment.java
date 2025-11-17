package com.hallbooking.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment now displays a welcome message and a sliding window of reviews.
 */
public class homeFragment extends Fragment {

    private appTitleInterface appTitleInterface;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private final Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    public homeFragment() {
        // Required empty public constructor
    }

    public static homeFragment newInstance() {
        return new homeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the new welcome layout for this fragment
        return inflater.inflate(R.layout.fragment_home_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.reviews_view_pager);
        tabLayout = view.findViewById(R.id.reviews_tab_layout);

        // Create sample reviews
        List<String> reviews = new ArrayList<>();
        reviews.add("\"Fantastic app! Made booking a hall for our family function incredibly simple and quick.\"");
        reviews.add("\"The process was seamless and the hall was exactly as advertised. Highly recommended!\"");
        reviews.add("\"A wide variety of halls to choose from. The owner was very responsive. Great experience overall.\"");
        reviews.add("\"I love how easy it is to list my property. I started getting inquiries almost immediately.\"");

        // Set up the adapter
        ReviewAdapter adapter = new ReviewAdapter(reviews);
        viewPager.setAdapter(adapter);

        // Attach the TabLayout to the ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // This is where you would customize the tab if you wanted to
        }).attach();

        // Setup the auto-scrolling runnable
        sliderRunnable = () -> {
            if (viewPager.getAdapter() != null) {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= viewPager.getAdapter().getItemCount()) {
                    nextItem = 0; // Loop back to the start
                }
                viewPager.setCurrentItem(nextItem, true);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appTitleInterface != null) {
            appTitleInterface.onSetTitle("Home");
        }
        // Start auto-scrolling
        sliderHandler.postDelayed(sliderRunnable, 3000);

        // Add a callback to resume auto-scrolling after manual swipe
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop auto-scrolling
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}
