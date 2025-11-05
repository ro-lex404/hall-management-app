package adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikith_shetty.vgroup.R;

import java.util.List;

import models.EventData;

/**
 * This adapter is responsible for displaying a list of items on the home screen.
 * The text has been changed to reflect 'Bookings' instead of 'Events'.
 */
public class RVAdapter_home extends RecyclerView.Adapter<RVAdapter_home.ViewHolder> {
    private Listener listener;
    private List<EventData> eventDataList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public RVAdapter_home(List<EventData> list) {
        eventDataList = list;
    }

    @Override
    public RVAdapter_home.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_home, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        createBookingCard(holder, position);
    }

    @Override
    public int getItemCount() {
        return eventDataList.size();
    }

    private void createBookingCard(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView purpose = cardView.findViewById(R.id.card_event_name);
        TextView department = cardView.findViewById(R.id.card_event_college);
        TextView fee = cardView.findViewById(R.id.card_event_fee);
        TextView details = cardView.findViewById(R.id.card_event_details);
        TextView hall = cardView.findViewById(R.id.card_event_venue);

        // Hide the unused coordinator info text view
        cardView.findViewById(R.id.card_event_coordInfo).setVisibility(View.GONE);

        EventData event = eventDataList.get(position);

        purpose.setText("Purpose: " + event.getEventName());
        department.setText("Department: " + event.getCollege());
        fee.setText("Fee: " + event.getFee());
        details.setText("Notes: " + event.getDetails());
        if (event.getVenue() != null) {
            hall.setText("Hall: " + event.getVenue().getArea());
        } else {
            hall.setText("Hall: Not Assigned");
        }

        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(eventDataList.get(position));
            }
        });
    }

    public interface Listener {
        void onClick(EventData data);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
