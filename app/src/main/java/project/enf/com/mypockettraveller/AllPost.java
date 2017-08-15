package project.enf.com.mypockettraveller;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import Adapters.AllPostAdapter;
import Adapters.TravelListAdapter;
import Database.DatabaseOpenHelper;
import Models.Post;
import Models.Travel;

import static android.R.attr.data;

public class AllPost extends ListFragment {

    private ArrayList<Post> data = new ArrayList<>();
    private DatabaseReference mdb = FirebaseDatabase.getInstance().getReference();
    private AllPostAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);

        mdb.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();

                Log.v("count: ", String.valueOf(dataSnapshot.getChildrenCount()));

                for (DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    System.out.println(postSnapShot.getValue().toString());
                    HashMap<String, Object> postData = (HashMap<String, Object>) postSnapShot.getValue();
                    Post post = new Post(postData);
                    data.add(post);
                    System.out.println (data.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        System.out.println (data.size());

        adapter = new AllPostAdapter(getActivity(), R.layout.item_post, data);
        setListAdapter(adapter);

        adapter.notifyDataSetChanged();

        return rootView;
    }

}