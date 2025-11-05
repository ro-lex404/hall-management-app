package adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hallbooking.app.R;

import java.util.List;

import helper.classes.Global;
import models.EventData;

/**
 * This adapter is responsible for displaying a list of bookings.
 * It has been refactored from the original RVAdapter_events.
 */
public class RVAdapter_bookings extends RecyclerView.Adapter<RVAdapter_bookings.ViewHolder> {
    private Listener listener;
    private List<EventData> eventDataList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView view){
            super(view);
            cardView = view;
        }
    }

    public RVAdapter_bookings(List<EventData> list){
        eventDataList = list;
    }

    @Override
    public RVAdapter_bookings.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_events,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            createBookingsCard(holder, position);
    }

    @Override
    public int getItemCount() {
        return eventDataList.size();
    }

    private void createBookingsCard(ViewHolder holder, final int position){
        //setup view
        CardView cardView = holder.cardView;
        TextView purpose = (TextView)cardView.findViewById(R.id.card_event_name);
        TextView department = (TextView)cardView.findViewById(R.id.card_event_college);
        TextView fee = (TextView)cardView.findViewById(R.id.card_event_fee);
        TextView details = (TextView)cardView.findViewById(R.id.card_event_details);
        TextView coordInfo = (TextView)cardView.findViewById(R.id.card_event_coordInfo);
        TextView venue = (TextView)cardView.findViewById(R.id.card_event_venue);
        purpose.setText("Purpose: " + eventDataList.get(position).getEventName());
        department.setText("Department: " + eventDataList.get(position).getCollege());
        fee.setText("Fee: " + eventDataList.get(position).getFee());
        details.setText("Notes: " + eventDataList.get(position).getDetails());
        //coordInfo.setText(eventDataList.get(position).getCoordinatorInfo());
        venue.setText("Hall: " + eventDataList.get(position).getVenue().getArea());
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onClick(eventDataList.get(position));
            }
        });
    }

    public interface Listener{
        void onClick(EventData data);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
}
