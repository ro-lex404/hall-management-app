package adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hallbooking.app.R;
import java.util.List;
import models.EventData;

public class RVAdapter_events extends RecyclerView.Adapter<RVAdapter_events.ViewHolder> {
    private Listener listener;
    private List<EventData> eventDataList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public RVAdapter_events(List<EventData> list) {
        eventDataList = list;
    }

    @Override
    public RVAdapter_events.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_events, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        createEventsCard(holder, position);
    }

    @Override
    public int getItemCount() {
        return eventDataList.size();
    }

    private void createEventsCard(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView eventName = cardView.findViewById(R.id.card_event_name);
        TextView college = cardView.findViewById(R.id.card_event_college);
        TextView fee = cardView.findViewById(R.id.card_event_fee);
        TextView details = cardView.findViewById(R.id.card_event_details);
        TextView coordInfo = cardView.findViewById(R.id.card_event_coordInfo);
        TextView venue = cardView.findViewById(R.id.card_event_venue);

        EventData currentHall = eventDataList.get(position);

        eventName.setText(currentHall.getEventName());
        college.setText(currentHall.getCollege());
        fee.setText(currentHall.getFee());
        details.setText(currentHall.getDetails());

        if (currentHall.getVenue() != null && currentHall.getVenue().getArea() != null) {
            venue.setText(currentHall.getVenue().getArea());
        } else {
            venue.setText("Location not available");
        }

        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(currentHall);
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
