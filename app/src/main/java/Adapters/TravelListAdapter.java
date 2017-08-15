package Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Models.Travel;
import project.enf.com.mypockettraveller.R;
import project.enf.com.mypockettraveller.ViewTravelDetails;

/**
 * Created by shafi on 7/17/2017.
 */

public class TravelListAdapter extends ArrayAdapter {

    private ArrayList<Travel> travelList ;
    private Activity activity;
    private int layoutResouce;

    public TravelListAdapter(Activity act, int resource, ArrayList<Travel> data){
        super (act, resource, data);
        layoutResouce = resource;
        activity = act;
        travelList = data;
    }

    @Override
    public int getCount() {
        return travelList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return travelList.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        TravelViewHolder holder = null;

        if (holder == null || row.getTag() == null){
            LayoutInflater inflater =  LayoutInflater.from(activity);
            row = inflater.inflate(layoutResouce, null);

            holder = new TravelViewHolder();

            holder.location = (TextView) row.findViewById(R.id.listRowPlaceNameText);
            holder.dateAdded = (TextView) row.findViewById(R.id.listRowDateText);
            holder.duration = (TextView) row.findViewById(R.id.listRowDurationText);
            holder.rating = (TextView) row.findViewById(R.id.listRowRatingText);
            holder.title = (TextView) row.findViewById(R.id.listRowTravelTypeText);

            row.setTag(holder);

        }
        else{
            holder = (TravelViewHolder) row.getTag();
        }

        holder.travel = (Travel) getItem(position);

        holder.title.setText(holder.travel.getTitle());
        holder.duration.setText(String.valueOf(holder.travel.getDuration()));
        holder.rating.setText(String.valueOf(holder.travel.getRating()));
        holder.dateAdded.setText(holder.travel.getDateAdded());
        holder.location.setText(holder.travel.getLocation());

        final TravelViewHolder finalHolder = holder;
        row.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ViewTravelDetails.class);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable("data", finalHolder.travel);
                i.putExtras(mBundle);

                activity.startActivity(i);
            }
        });

        return row;
    }

    public class TravelViewHolder{

        Travel travel;
        TextView location;
        TextView dateAdded;
        TextView duration;
        TextView title;
        TextView rating;

    }
}
