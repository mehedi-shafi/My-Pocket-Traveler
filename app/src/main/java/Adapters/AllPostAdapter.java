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

import Models.Post;
import Models.Travel;
import Utilities.UtilityFunctions;
import project.enf.com.mypockettraveller.ActivityPostDetail;
import project.enf.com.mypockettraveller.R;
import project.enf.com.mypockettraveller.ViewTravelDetails;

/**
 * Created by shafi on 8/14/2017.
 */

public class AllPostAdapter extends ArrayAdapter {

    private Activity activity;
    private int layoutResourse;
    private ArrayList<Post> data;

    public AllPostAdapter(Activity ac, int layoutResource, ArrayList<Post> data){
        super (ac, layoutResource, data);

        this.activity = ac;
        this.data = data;
        this.layoutResourse = layoutResource;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return data.get(position);
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
        TravelViewHolderS holder = null;

        if (holder == null || row.getTag() == null){
            LayoutInflater inflater =  LayoutInflater.from(activity);

            row = inflater.inflate(layoutResourse, null);

            holder = new TravelViewHolderS();

            holder.title = (TextView) row.findViewById(R.id.post_title);
            holder.author = (TextView) row.findViewById(R.id.poster);
            holder.stars = (TextView) row.findViewById(R.id.stars);
            holder.description = (TextView) row.findViewById(R.id.description);

            row.setTag(holder);

        }
        else{
            holder = (TravelViewHolderS) row.getTag();
        }

        holder.post = (Post) getItem(position);

        holder.title.setText(holder.post.getTitle());
        holder.author.setText(holder.post.getAuthor());
        holder.stars.setText(new UtilityFunctions().getStarBackFromString(holder.post.getStarCount()));
        holder.description.setText(holder.post.getBody());

        final TravelViewHolderS finalHolder = holder;
        row.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ActivityPostDetail.class);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable("data", finalHolder.post);
                i.putExtras(mBundle);

                activity.startActivity(i);
            }
        });

        return row;
    }

    public class TravelViewHolderS{
        Post post;

        TextView title;
        TextView author;
        TextView description;
        TextView stars;
    }

}
