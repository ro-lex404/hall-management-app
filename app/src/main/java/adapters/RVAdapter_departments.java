package adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikith_shetty.vgroup.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This adapter is responsible for displaying a list of departments.
 * It has been refactored from the original RVAdapter_colleges.
 */
public class RVAdapter_departments extends RecyclerView.Adapter<RVAdapter_departments.ViewHolder> {
    private Listener listener;
    JSONArray jsonArray;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView view){
            super(view);
            cardView = view;
        }
    }

    public RVAdapter_departments(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }

    @Override
    public RVAdapter_departments.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;
        cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_single_textview,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        createDepartmentsCard(holder, position);
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    private void createDepartmentsCard(ViewHolder holder, final int position){
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
