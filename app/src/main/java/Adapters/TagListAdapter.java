package Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import project.enf.com.mypockettraveller.R;
import project.enf.com.mypockettraveller.TaggedTravels;

/**
 * Created by shafi on 7/18/2017.
 */

public class TagListAdapter extends ArrayAdapter {

    private int layoutResource;
    private Activity activity;
    private ArrayList<String> tags = new ArrayList<>();

    public TagListAdapter(Activity act, int resource, ArrayList<String> data){

        super(act, resource, data);

        this.layoutResource = resource;
        this.activity = act;
        this.tags = data;

    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return tags.get(position);
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
        TagViewHolder holder = null;

        if (row == null || row.getTag() == null){

            LayoutInflater inflater = LayoutInflater.from(activity );
            row = inflater.inflate(layoutResource, null);

            holder = new TagViewHolder();

            holder.tag = (TextView) row.findViewById(R.id.tagRowTag);

            row.setTag(holder);
        }
        else{
            holder = (TagViewHolder) row.getTag();
        }

        holder.tagText = (String) getItem(position);

        holder.tag.setText(holder.tagText);

        final TagViewHolder finalHolder = holder;
        row.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, TaggedTravels.class);
                i.putExtra("tag_tag", finalHolder.tagText);

                activity.startActivity(i);
            }
        });

        return row;
    }

    public class TagViewHolder{

        TextView tag;
        String tagText;

    }
}
