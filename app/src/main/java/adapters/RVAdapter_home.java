package adapters;

import android.net.Uri;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hallbooking.app.R;

import java.util.List;

import models.EventData;

/**
 * This adapter now displays Hall information, including a preview image.
 */
public class RVAdapter_home extends RecyclerView.Adapter<RVAdapter_home.ViewHolder> {
    private Listener listener;
    private List<EventData> hallList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public RVAdapter_home(List<EventData> list) {
        hallList = list;
    }

    public void updateData(List<EventData> newHallList) {
        if (hallList != null) {
            hallList.clear();
            hallList.addAll(newHallList);
        } else {
            hallList = newHallList;
        }
        notifyDataSetChanged();
    }

    @Override
    public RVAdapter_home.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_home, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        createHallCard(holder, position);
    }

    @Override
    public int getItemCount() {
        if (hallList == null) {
            return 0;
        }
        return hallList.size();
    }

    private void createHallCard(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView hallImage = cardView.findViewById(R.id.card_hall_image);
        TextView hallName = cardView.findViewById(R.id.card_event_name);
        TextView location = cardView.findViewById(R.id.card_event_venue);
        TextView capacity = cardView.findViewById(R.id.card_event_details);
        TextView contact = cardView.findViewById(R.id.card_event_college);
        TextView fee = cardView.findViewById(R.id.card_event_fee);

        cardView.findViewById(R.id.card_event_coordInfo).setVisibility(View.GONE);
        fee.setVisibility(View.VISIBLE);

        EventData hall = hallList.get(position);

        // Load image safely
        String imageUriString = hall.getImageUrl();
        try {
            if (imageUriString != null && !imageUriString.isEmpty()) {
                hallImage.setImageURI(Uri.parse(imageUriString));
            } else {
                hallImage.setImageResource(R.mipmap.ic_launcher);
            }
        } catch (SecurityException e) {
            Log.e("RVAdapter_home", "Permission denied for image URI: " + imageUriString, e);
            hallImage.setImageResource(R.mipmap.ic_launcher); // Fallback to a default image
        }

        hallName.setText(hall.getEventName());
        if (hall.getVenue() != null) {
            location.setText(hall.getVenue().getArea() + ", " + hall.getVenue().getCity());
        }
        capacity.setText("Capacity: " + hall.getDetails());
        contact.setText("Contact: " + hall.getCollege());
        fee.setText("Booking Fee: " + hall.getFee());

        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(hallList.get(position));
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
