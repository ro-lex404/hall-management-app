package adapters;

import android.content.Context;
import android.net.Uri;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hallbooking.app.R;

import java.io.File;
import java.util.List;

import models.EventData;

public class RVAdapter_home extends RecyclerView.Adapter<RVAdapter_home.ViewHolder> {
    private Listener listener;
    private List<EventData> hallList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public RVAdapter_home(List<EventData> list, Context context) {
        hallList = list;
        this.context = context;
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
        if (hallList == null) return 0;
        return hallList.size();
    }

    private void createHallCard(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView hallImage = cardView.findViewById(R.id.card_hall_image);
        TextView hallName = cardView.findViewById(R.id.card_event_name);
        RatingBar ratingBar = cardView.findViewById(R.id.card_average_rating);
        TextView location = cardView.findViewById(R.id.card_event_venue);
        TextView capacity = cardView.findViewById(R.id.card_event_details);
        TextView contact = cardView.findViewById(R.id.card_event_college);
        TextView fee = cardView.findViewById(R.id.card_event_fee);

        cardView.findViewById(R.id.card_event_coordInfo).setVisibility(View.GONE);
        fee.setVisibility(View.VISIBLE);

        EventData hall = hallList.get(position);

        String imageFileName = hall.getImageUrl();
        if (imageFileName != null && !imageFileName.isEmpty()) {
            File imageFile = new File(context.getFilesDir(), imageFileName);
            if (imageFile.exists()) {
                hallImage.setImageURI(Uri.fromFile(imageFile));
            } else {
                hallImage.setImageResource(R.mipmap.ic_launcher);
            }
        } else {
            hallImage.setImageResource(R.mipmap.ic_launcher);
        }

        hallName.setText(hall.getEventName());
        ratingBar.setRating(hall.getAverageRating());
        
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
