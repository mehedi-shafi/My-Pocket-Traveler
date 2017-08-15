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

import java.util.ArrayList;

import Models.Travel;
import project.enf.com.mypockettraveller.R;
import project.enf.com.mypockettraveller.ViewTravelDetails;

/**
 * Created by shafi on 7/18/2017.
 */

public class LookBackAdapter extends ArrayAdapter{

    private int layoutResource;
    private Activity activity;
    private ArrayList<Travel> lookbackList = new ArrayList<>();


    public LookBackAdapter(Activity act, int resource, ArrayList<Travel> data){
        super (act, resource, data);

        this.activity = act;
        this.layoutResource = resource;
        this.lookbackList = data;
    }

    @Override
    public int getCount() {
        return lookbackList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return lookbackList.get(position);
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
        LookbackViewHolder holder = null;

        if (row == null || row.getTag() == null){

            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layoutResource, null);

            holder = new LookbackViewHolder();

            holder.location = (TextView) row.findViewById(R.id.lookBackRowPlace);
            holder.date = (TextView) row.findViewById(R.id.lookBackRowDate);
            holder.title = (TextView) row.findViewById(R.id.lookBackRowTitle);

            row.setTag(holder);

        }
        else{
            holder = (LookbackViewHolder) row.getTag();
        }

        holder.travel = (Travel) getItem(position);

        holder.date.setText(holder.travel.getDateAdded());
        holder.title.setText(holder.travel.getTitle());
        holder.location.setText(holder.travel.getLocation());

        final LookbackViewHolder finalHolder = holder;
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

    public class LookbackViewHolder{

        Travel travel;
        TextView location;
        TextView date;
        TextView title;

    }
}
