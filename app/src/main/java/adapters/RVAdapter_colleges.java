package adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hallbooking.app.R;

import org.json.JSONArray;
import org.json.JSONException;

public class RVAdapter_colleges extends RecyclerView.Adapter<RVAdapter_colleges.ViewHolder> {
    private Listener listener;
    JSONArray jsonArray;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView view){
            super(view);
            cardView = view;
        }
    }

    public RVAdapter_colleges(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }

    @Override
    public RVAdapter_colleges.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;
        cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_single_textview,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        createPlacesCard(holder, position);
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    private void createPlacesCard(ViewHolder holder, final int position){
        //setup view
        CardView cardView = holder.cardView;
        TextView places = (TextView)cardView.findViewById(R.id.card_single_textview);
        try {
            places.setText(jsonArray.get(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(listener != null) try {
                    listener.onClick(jsonArray.get(position).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface Listener{
        void onClick(String data);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
}
