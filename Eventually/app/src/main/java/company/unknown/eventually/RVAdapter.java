package company.unknown.eventually;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventsViewHolder>{

    public static class EventsViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView eventName;
        TextView eventTime;
        TextView eventLocation;

        public EventsViewHolder(View itemView){
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.eventsListView);
            eventName=(TextView)itemView.findViewById(R.id.eventNameShow);
            eventTime=(TextView)itemView.findViewById(R.id.timeShow);
            eventLocation=(TextView)itemView.findViewById(R.id.locationShow);
        }

    }

    List<EventsEntity> eventsEntityList;

    RVAdapter(List<EventsEntity> eventsEntityList){
        this.eventsEntityList=eventsEntityList;
    }


    @Override
    public RVAdapter.EventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardviewlayout, viewGroup, false);
        EventsViewHolder evh = new EventsViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(RVAdapter.EventsViewHolder holder, int i) {
        holder.eventName.setText(eventsEntityList.get(i).name);
        holder.eventTime.setText(eventsEntityList.get(i).date);
        holder.eventLocation.setText(eventsEntityList.get(i).locationid);
    }

    @Override
    public int getItemCount() {
        return eventsEntityList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}